package com.mall.mallproduct.spu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SpuDTO {

    @NotBlank(message = "商品名称不能为空")
    private String name;

    private String image;

    @NotNull(message = "品牌ID不能为空")
    private Long brandId;

    @NotNull(message = "分类不能为空")
    private Long categoryId;

    private String description;

    /**
     * 上架状态：1 在售、0 下架，为空时默认下架
     */
    private Integer status;
}