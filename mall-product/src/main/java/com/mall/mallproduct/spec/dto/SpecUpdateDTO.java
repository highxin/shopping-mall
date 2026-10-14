package com.mall.mallproduct.spec.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SpecUpdateDTO {
    @NotNull(message = "规格ID不能为空")
    private Long id;
    // 如果允许改所属SPU，才需要 spuId；否则可以不要
    @NotNull(message = "SPU ID不能为空")
    private Long spuId;
    @NotBlank(message = "规格名称不能为空")
    private String name;
    private Integer sort;
}