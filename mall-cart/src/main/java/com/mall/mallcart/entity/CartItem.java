package com.mall.mallcart.entity;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class CartItem {


    /**
     * SKU ID
     */
    private Long skuId;


    /**
     * 商品名称
     */
    private String skuName;


    /**
     * 商品价格
     */
    private BigDecimal price;


    /**
     * 商品数量
     */
    private Integer quantity;


}