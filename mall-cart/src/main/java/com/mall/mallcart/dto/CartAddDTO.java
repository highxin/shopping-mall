package com.mall.mallcart.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CartAddDTO {


    /**
     * SKU ID
     */
    private Long skuId;


    /**
     * 购买数量
     */
    @Min(value = 1,message = "购买数量不能小于1")
    private Integer quantity;

}