package com.mall.mallproduct.category.entity;


import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("category")
public class Category {


    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    private String name;


    /**
     * 父分类id
     */
    private Long parentId;


    /**
     * 排序
     */
    private Integer sort;


    /**
     * 状态
     */
    private Integer status;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;

}