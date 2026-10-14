package com.mall.mallorder.vo;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class OrderItemVO {


    /**
     * 商品id
     */
    private Long skuId;


    /**
     * 商品名称快照
     */
    private String skuName;


    /**
     * 商品图片
     */
    private String image;


    /**
     * 商品规格
     */
    private String specInfo;


    /**
     * 商品价格
     */
    private BigDecimal price;


    /**
     * 数量
     */
    private Integer quantity;


    /**
     * 商品总金额
     */
    private BigDecimal totalAmount;

}