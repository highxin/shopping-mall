package com.mall.mallproduct.sku.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.dto.SkuStockDTO;
import com.mall.common.exception.BusinessException;
import com.mall.common.utils.HashUtils;
import com.mall.mallproduct.sku.dto.*;
import com.mall.mallproduct.sku.vo.SkuDetailVO;
import com.mall.mallproduct.sku.vo.SkuVO;
import com.mall.mallproduct.skuspec.entity.SkuSpec;
import com.mall.mallproduct.skuspec.mapper.SkuSpecMapper;
import com.mall.mallproduct.specvalue.entity.SpecValue;
import com.mall.mallproduct.specvalue.mapper.SpecValueMapper;
import com.mall.mallproduct.spu.entity.Spu;
import com.mall.mallproduct.spu.mapper.SpuMapper;
import com.mall.mallproduct.sku.entity.Sku;
import com.mall.mallproduct.sku.mapper.SkuMapper;
import com.mall.mallproduct.sku.service.SkuService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService {

    private final SpuMapper spuMapper;

    private final SpecValueMapper specValueMapper;

    private final SkuSpecMapper skuSpecMapper;
    private final SkuMapper skuMapper;

    public SkuServiceImpl(SpuMapper spuMapper, SpecValueMapper specValueMapper, SkuSpecMapper skuSpecMapper, SkuMapper skuMapper) {
        this.spuMapper = spuMapper;
        this.specValueMapper = specValueMapper;
        this.skuSpecMapper = skuSpecMapper;
        this.skuMapper = skuMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        Sku sku = getById(id);
        if (sku == null) {
            throw new BusinessException("SKU不存在");
        }
        // 删除SKU规格关系
        LambdaQueryWrapper<SkuSpec> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuSpec::getSkuId, id);
        // 删除SKU规格关系
        skuSpecMapper.delete(wrapper);
        // 删除SKU
        removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSku(SkuUpdateDTO skuUpdateDTO) {
        Sku existSku = getById(skuUpdateDTO.getId());
        if (existSku == null) {
            throw new BusinessException("SKU不存在");
        }
        // 删除规格关系
        LambdaQueryWrapper<SkuSpec> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuSpec::getSkuId, skuUpdateDTO.getId());
        skuSpecMapper.delete(wrapper);
        // 更新SKU信息
        BeanUtils.copyProperties(skuUpdateDTO, existSku,"id");
        // 构建SKU规格哈希值
        String hash = HashUtils.generateSkuHash(skuUpdateDTO.getSpecValueIds());
        existSku.setSpecHash(hash);
        updateById(existSku);

        // 构建SKU规格值
        List<SpecValue> specValues =
                specValueMapper.selectBatchIds(
                        skuUpdateDTO.getSpecValueIds()
                );
        List<SkuSpec> skuSpecs = new ArrayList<>();
        for(SpecValue value : specValues){
            SkuSpec skuSpec = new SkuSpec();
            // 关联SKU
            skuSpec.setSkuId(skuUpdateDTO.getId());
            // 关联规格
            skuSpec.setSpecId(
                    value.getSpecId()
            );
            // 关联规格值
            skuSpec.setSpecValueId(
                    value.getId()
            );
            skuSpecs.add(skuSpec);
        }
        // 保存SKU规格关系
        for(SkuSpec skuSpec : skuSpecs){
            skuSpecMapper.insert(skuSpec);
        }
    }

    @Override
    public List<Sku> getSkuList(Long spuId) {
        LambdaQueryWrapper<Sku> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Sku::getSpuId, spuId);
        return list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSku(SkuDTO skuDTO) {
        Spu spu = spuMapper.selectById(skuDTO.getSpuId());
        if (spu == null) {
            throw new BusinessException("SPU不存在");
        }
        Sku sku = new Sku();
        BeanUtils.copyProperties(skuDTO, sku);
        // 构建SKU规格哈希值
        String hash = HashUtils.generateSkuHash(skuDTO.getSpecValueIds());
        sku.setSpecHash(hash);
        save(sku);

        Long skuId = sku.getId();
        // 获取SKU对应规格值的ID[1,3]
        List<Long> specValueIds = skuDTO.getSpecValueIds();
        // 根据ID查询规格值
        List<SpecValue> specValues = specValueMapper.selectBatchIds(specValueIds);
        // 构建SKU规格值
        List<SkuSpec> skuSpecs = new ArrayList<>();
        for(SpecValue value : specValues){
            SkuSpec skuSpec = new SkuSpec();
            // 关联SKU
            skuSpec.setSkuId(skuId);
            // 关联规格
            skuSpec.setSpecId(
                    value.getSpecId()
            );
            // 关联规格值
            skuSpec.setSpecValueId(
                    value.getId()
            );
            skuSpecs.add(skuSpec);
        }
        // 保存SKU规格关系
        for(SkuSpec skuSpec : skuSpecs){
            skuSpecMapper.insert(skuSpec);
        }

    }

    @Override
    public IPage<SkuVO> pageSku(Integer pageNum, Integer pageSize, SkuQueryDTO skuQueryDTO) {
        Page<SkuVO> page = new Page<>(pageNum, pageSize);
        return skuMapper.selectSkuPage(page, skuQueryDTO);
    }

    @Override
    public SkuDetailVO getSkuDetail(Long id){
        List<SkuDetailVO> list =
                skuMapper.selectSkuDetail(id);
        if(list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

    @Override
    public void updateStatus(SkuStatusDTO dto) {
        Sku sku = getById(dto.getId());
        if (sku == null) {
            throw new BusinessException("SKU不存在");
        }
        sku.setStatus(dto.getStatus());
        updateById(sku);
    }

    @Override
    public void updateStock(SkuStockDTO skuStockDTO) {
        Sku sku = getById(skuStockDTO.getSkuId());
        if (sku == null) {
            throw new BusinessException("SKU不存在");
        }
        sku.setStock(skuStockDTO.getQuantity());
        updateById(sku);
    }

    @Transactional
    @Override
    public void deductStock(List<SkuStockDTO> list) {
        for(SkuStockDTO dto : list){
            System.out.println("扣减 skuId=" + dto.getSkuId() + " quantity=" + dto.getQuantity());
            int rows = skuMapper.deductStock(dto.getSkuId(),dto.getQuantity());
            System.out.println("影响行数 rows=" + rows);
            if(rows<=0){
                throw new BusinessException("扣减库存失败");
            }
        }
    }

    @Transactional
    @Override
    public void addStock(List<SkuStockDTO> list) {
        for(SkuStockDTO dto : list){
            int rows = skuMapper.addStock(dto.getSkuId(),dto.getQuantity());
            if(rows<=0){
                throw new BusinessException("回补库存失败");
            }
        }
    }

}