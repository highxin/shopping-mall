package com.mall.mallproduct.sku.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuDetailVO {


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
     * 商品图片
     */
    private String image;


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


    /**
     * SKU规格
     */
    private List<SkuSpecVO> specs;

}