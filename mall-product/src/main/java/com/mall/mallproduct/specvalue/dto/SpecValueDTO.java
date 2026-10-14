package com.mall.mallproduct.specvalue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SpecValueDTO {

    @NotNull(message = "规格ID不能为空")
    private Long specId;

    @NotBlank(message = "规格值不能为空")
    private String value;

    private Integer sort;
}