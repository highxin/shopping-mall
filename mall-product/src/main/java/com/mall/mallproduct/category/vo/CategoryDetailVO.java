package com.mall.mallproduct.category.vo;

import lombok.Data;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/8/27
 */
@Data
public class CategoryDetailVO {

    private Long id;

    private String name;

    private Long parentId;

    private Integer sort;

}
