package com.mall.mallproduct.product.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("product_brand")
public class Brand {


    /**
     * 品牌ID
     */
    @TableId(
            value = "id",
            type = IdType.AUTO
    )
    private Long id;


    /**
     * 品牌名称
     */
    @NotBlank(message="品牌名称不能为空")
    private String name;


    /**
     * 品牌logo
     */
    private String logo;

    /**
     * 逻辑删除字段
     */
    @TableLogic
    private Integer deleted;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}