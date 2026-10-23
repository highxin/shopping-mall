package com.mall.mallproduct.spu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.enums.StatusEnum;
import com.mall.common.exception.BusinessException;
import com.mall.mallproduct.category.entity.Category;
import com.mall.mallproduct.category.service.CategoryService;
import com.mall.common.dto.ProductIndexDTO;
import com.mall.mallproduct.product.entity.Brand;
import com.mall.mallproduct.product.service.BrandService;
import com.mall.mallproduct.sku.entity.Sku;
import com.mall.mallproduct.sku.service.SkuService;
import com.mall.mallproduct.spu.dto.SpuDTO;
import com.mall.mallproduct.spu.dto.SpuQueryDTO;
import com.mall.mallproduct.spu.dto.SpuUpdateDTO;
import com.mall.mallproduct.spu.entity.Spu;
import com.mall.mallproduct.spu.mapper.SpuMapper;
import com.mall.mallproduct.spu.service.SpuService;
import com.mall.mallproduct.spu.vo.SpuVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/9/2
 */
@Service
public class SpuServiceImpl extends ServiceImpl<SpuMapper, Spu> implements SpuService {
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final SkuService skuService;

    public SpuServiceImpl(BrandService brandService, CategoryService categoryService, SkuService skuService) {
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.skuService = skuService;
    }

    @Override
    public void saveSpu(SpuDTO spuDTO) {
        // 校验品牌是否存在
        checkBrandAndCategoryExist(spuDTO.getBrandId(), spuDTO.getCategoryId());

        Spu spu = new Spu();
        BeanUtils.copyProperties(spuDTO, spu);
        save(spu);
    }

    @Override
    public IPage<SpuVO> pageSpu(Integer page, Integer size, SpuQueryDTO queryDTO) {
        // 1.创建分页
        Page<Spu> SpuPage = new Page<>(page, size);

        LambdaQueryWrapper<Spu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(
                queryDTO.getName() != null,
                Spu::getName,
                queryDTO.getName()
        );
        queryWrapper.eq(
                queryDTO.getBrandId() != null,
                Spu::getBrandId,
                queryDTO.getBrandId()
        );
        queryWrapper.eq(
                queryDTO.getCategoryId() != null,
                Spu::getCategoryId,
                queryDTO.getCategoryId()
        );
        queryWrapper.eq(
                queryDTO.getStatus() != null,
                Spu::getStatus,
                queryDTO.getStatus()
        );
        // 2. 分页查询
        IPage<Spu> result = page(SpuPage, queryWrapper);
        // 3. Entity 转 VO
        return result.convert(spu -> {
            SpuVO spuVO = new SpuVO();
            BeanUtils.copyProperties(spu, spuVO);
            return spuVO;
        });
    }

    @Override
    public SpuVO getSpuDetail(Long id) {
        Spu spu = getById(id);
        if (spu == null) {
            throw new BusinessException("SPU不存在");
        }
        SpuVO spuVO = new SpuVO();
        BeanUtils.copyProperties(spu, spuVO);
        return spuVO;
    }

    @Override
    public void updateSpu(SpuUpdateDTO spuUpdateDTO) {
        Spu spu = getById(spuUpdateDTO.getId());
        if (spu == null) {
            throw new BusinessException("SPUId不存在");
        }
        // 校验品牌和分类是否存在
        checkBrandAndCategoryExist(spuUpdateDTO.getBrandId(), spuUpdateDTO.getCategoryId());

        BeanUtils.copyProperties(spuUpdateDTO, spu);
        updateById(spu);
    }

    @Override
    public void deleteSpu(Long id) {
        Spu spu = getById(id);
        if (spu == null) {
            throw new BusinessException("SPU不存在");
        }
        removeById(id);
    }

    /**
     * 校验品牌和分类是否存在
     * @param brandId 品牌ID
     * @param categoryId 分类ID
     */
    private void checkBrandAndCategoryExist(Long brandId, Long categoryId) {
        Brand brand = brandService.getById(brandId);
        if (brand == null) {
            throw new BusinessException("品牌不存在");
        }
        // 校验分类是否存在
        Category category = categoryService.getById(categoryId);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
    }

    @Override
    public List<ProductIndexDTO> listForSearch() {

        // 1. 查询所有未删除的 SPU
        List<Spu> spuList = list();

        if (spuList.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 获取所有 SPU ID
        List<Long> spuIds = spuList.stream()
                .map(Spu::getId)
                .collect(Collectors.toList());

        // 3. 一次性查询这些 SPU 对应的 SKU
        List<Sku> skuList = skuService.list(
                new LambdaQueryWrapper<Sku>()
                        .in(Sku::getSpuId, spuIds)
        );

        // 4. 按 SPU ID 分组
        Map<Long, List<Sku>> skuMap = skuList.stream()
                .filter(sku -> Objects.equals(sku.getStatus(), StatusEnum.SELL_UP.getCode()))
                .collect(Collectors.groupingBy(Sku::getSpuId));

        // 5. 将 SPU 和 SKU 汇总成商品索引数据
        return spuList.stream()
                .map(spu -> {
                    ProductIndexDTO dto = new ProductIndexDTO();

                    BeanUtils.copyProperties(spu, dto);

                    List<Sku> skus = skuMap.getOrDefault(
                            spu.getId(), Collections.emptyList()
                    );

                    if (!skus.isEmpty()) {
                        dto.setMinPrice(skus.stream()
                                .map(Sku::getPrice)
                                .filter(Objects::nonNull)
                                .min(BigDecimal::compareTo)
                                .orElse(null));

                        dto.setMaxPrice(skus.stream()
                                .map(Sku::getPrice)
                                .filter(Objects::nonNull)
                                .max(BigDecimal::compareTo)
                                .orElse(null));

                        dto.setTotalStock(skus.stream()
                                .map(Sku::getStock)
                                .filter(Objects::nonNull)
                                .mapToInt(Integer::intValue)
                                .sum());
                    } else {
                        dto.setTotalStock(0);
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

}