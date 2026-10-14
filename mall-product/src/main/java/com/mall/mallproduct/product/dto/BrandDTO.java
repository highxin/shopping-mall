package com.mall.mallproduct.product.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class BrandDTO {
    /**
     * 品牌ID
     */
    private Long id;

    /**
     * 品牌名称
     */
    @NotBlank(message="品牌名称不能为空")
    private String name;


    /**
     * logo地址
     */
    private String logo;

}