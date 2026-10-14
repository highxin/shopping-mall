package com.mall.mallproduct.category.vo;

import lombok.Data;

import java.util.List;

@Data
public class CategoryListVO {

    private Long id;

    private String name;

    private Long parentId;

    private Integer sort;


    /**
     * 子分类
     */
    private List<CategoryListVO> children;
}