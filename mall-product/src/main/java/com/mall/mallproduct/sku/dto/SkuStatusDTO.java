package com.mall.mallproduct.sku.dto;

import lombok.Data;

@Data
public class SkuStatusDTO {


    /**
     * SKU ID
     */
    private Long id;


    /**
     * 状态
     * 0 下架
     * 1 上架
     */
    private Integer status;

}