package com.mall.mallproduct.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/8/24
 */
@Data
public class CategoryDTO {

    /**
     * 分类id
     */
    private Long id;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    private String name;

    /**
     * 父分类id
     * 0表示一级分类
     */
    private Long parentId;

    /**
     * 分类排序
     */
    private Integer sort;
}
