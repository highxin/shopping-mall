package com.mall.mallproduct.sku.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.dto.SkuStockDTO;
import com.mall.mallproduct.sku.dto.*;
import com.mall.mallproduct.sku.entity.Sku;
import com.mall.mallproduct.sku.vo.SkuDetailVO;
import com.mall.mallproduct.sku.vo.SkuVO;

import java.util.List;

public interface SkuService extends IService<Sku> {

    /**
     * 删除SKU
     * @param id SKU ID
     */
    void deleteById(Long id);

    /**
     * 更新SKU
     * @param skuUpdateDTO skuUpdateDTO
     */
    void updateSku(SkuUpdateDTO skuUpdateDTO);

    /**
     * 获取SKU列表
     * @param spuId SPU ID
     * @return SKU列表
     */
    List<Sku> getSkuList(Long spuId);

    /**
     * 保存SKU
     * @param skuDTO SKU DTO
     */
    void saveSku(SkuDTO skuDTO);

    /**
     * 分页查询SKU
     * @param pageNum 当前页
     * @param pageSize 每页数量
     * @return 分页结果
     */
    IPage<SkuVO> pageSku(Integer pageNum, Integer pageSize, SkuQueryDTO skuQueryDTO);

    /**
     * 获取SKU详情
     * @param id SKU ID
     * @return SKU详情
     */
    SkuDetailVO getSkuDetail(Long id);

    /**
     * 更新SKU状态
     * @param dto SKU状态 DTO
     */
    void updateStatus(SkuStatusDTO dto);

    /**
     * 更新SKU库存
     * @param skuStockDTO sku库存DTO
     */
    void updateStock(SkuStockDTO skuStockDTO);

    /**
     * 扣减SKU库存
     * @param list sku库存DTO列表
     */
    void deductStock(List<SkuStockDTO> list);

    /**
     * 增加SKU库存
     * @param list sku库存DTO列表
     */
    void addStock(List<SkuStockDTO> list);
}