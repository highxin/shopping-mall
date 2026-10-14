package com.mall.mallproduct.spu.dto;

import lombok.Data;

@Data
public class SpuQueryDTO {

    /**
     * 商品名称
     */
    private String name;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 商品状态
     * 0：下架
     * 1：上架
     */
    private Integer status;
}