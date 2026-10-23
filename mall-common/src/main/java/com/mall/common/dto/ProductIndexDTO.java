package com.mall.common.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductIndexDTO {

    /**
     * SPU ID
     */
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品图片
     */
    private String image;

    /**
     * 品牌 ID
     */
    private Long brandId;

    /**
     * 分类 ID
     */
    private Long categoryId;

    /**
     * 商品描述
     */
    private String description;

    /**
     * SPU 状态
     */
    private Integer status;

    /**
     * 最低价格
     */
    private BigDecimal minPrice;

    /**
     * 最高价格
     */
    private BigDecimal maxPrice;

    /**
     * 总库存
     */
    private Integer totalStock;
}