package com.mall.search.document;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDocument {

    /**
     * 商品 SPU ID，作为 Elasticsearch 文档 ID
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
     * SPU 上架状态
     */
    private Integer status;

    /**
     * 所有有效 SKU 中的最低价格
     */
    private BigDecimal minPrice;

    /**
     * 所有有效 SKU 中的最高价格
     */
    private BigDecimal maxPrice;

    /**
     * 所有有效 SKU 的库存总和
     */
    private Integer totalStock;
}