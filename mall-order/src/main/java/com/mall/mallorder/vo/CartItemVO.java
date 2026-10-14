package com.mall.mallorder.vo;


import lombok.Data;

import java.math.BigDecimal;


@Data
public class CartItemVO {


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
     * 购买数量
     */
    private Integer quantity;

    /**
     * 是否选中
     */
    private Boolean checked;

}