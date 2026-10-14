package com.mall.mallcart.dto;

import lombok.Data;

@Data
public class CartUpdateDTO {


    /**
     * SKU ID
     */
    private Long skuId;


    /**
     * 修改后的数量
     */
    private Integer quantity;

}