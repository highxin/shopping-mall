package com.mall.mallproduct.sku.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SkuVO {


    /**
     * SKU ID
     */
    private Long id;


    /**
     * SPU ID
     */
    private Long spuId;


    /**
     * SPU名称
     */
    private String spuName;


    /**
     * SKU名称
     */
    private String name;


    /**
     * 价格
     */
    private BigDecimal price;


    /**
     * 库存
     */
    private Integer stock;


    /**
     * 状态
     */
    private Integer status;

}