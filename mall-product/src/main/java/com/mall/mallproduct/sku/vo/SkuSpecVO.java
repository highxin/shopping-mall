package com.mall.mallproduct.sku.vo;

import lombok.Data;

@Data
public class SkuSpecVO {


    /**
     * 规格名称
     */
    private String specName;


    /**
     * 规格值
     */
    private String specValue;


    /**
     * 规格值ID，编辑 SKU 时用于回显已选规格
     */
    private Long specValueId;

}