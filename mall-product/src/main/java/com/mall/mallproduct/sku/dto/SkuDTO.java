package com.mall.mallproduct.sku.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuDTO {


    /**
     * 所属SPU
     */
    @NotNull(message = "spuId不能为空")
    private Long spuId;


    /**
     * SKU名称
     *
     * 例如：
     * 小米15 黑色 256GB
     */
    @NotBlank(message = "SKU名称不能为空")
    private String name;


    /**
     * 价格
     */
    @NotNull(message = "价格不能为空")
    private BigDecimal price;


    /**
     * 库存
     */
    @NotNull(message = "库存不能为空")
    private Integer stock;


    /**
     * 状态
     */
    private Integer status;


    /**
     * SKU对应规格值
     *
     * 例如：
     *
     * 黑色 id=1
     * 256GB id=3
     *
     * [1,3]
     */
    @NotEmpty(message = "规格值不能为空")
    private List<Long> specValueIds;

}