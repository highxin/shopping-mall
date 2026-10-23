package com.mall.mallproduct.sku.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class SkuUpdateDTO {


    /**
     * SKU ID
     */
    private Long id;


    /**
     * SPU ID
     */
    private Long spuId;


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
     * 规格值ID
     */
    private List<Long> specValueIds;

}