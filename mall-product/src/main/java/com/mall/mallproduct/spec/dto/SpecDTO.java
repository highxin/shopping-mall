 package com.mall.mallproduct.spec.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/9/10
 */
@Data
public class SpecDTO {

    @NotNull(message = "SPU ID不能为空")
    private Long spuId;

    @NotBlank(message = "规格名称不能为空")
    private String name;

    private Integer sort;
}
