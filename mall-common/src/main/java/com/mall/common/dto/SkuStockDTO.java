package com.mall.common.dto;


import lombok.Data;


@Data
public class SkuStockDTO {


    /**
     * SKU ID
     */
    private Long skuId;


    /**
     * 修改库存
     */
    private Integer quantity;

}