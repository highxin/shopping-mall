package com.mall.mallproduct.specvalue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SpecValueUpdateDTO {

    @NotNull(message = "id不能为空")
    private Long id;

    @NotBlank(message = "规格值不能为空")
    private String value;

    private Integer sort;
}