package com.mall.mallproduct.sku.dto;

import lombok.Data;


@Data
public class SkuQueryDTO {


    /**
     * SKU名称
     */
    private String name;


    /**
     * SPU ID
     */
    private Long spuId;


    /**
     * SKU状态
     *
     * 1 上架
     * 0 下架
     */
    private Integer status;

}