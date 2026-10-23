package com.mall.mallproduct.spu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("product_spu")
public class Spu {

    @TableId(type = IdType.AUTO)
    private Long id;


    private String name;

    private String image;


    private Long brandId;


    private Long categoryId;


    private String description;


    private Integer status;


    @TableLogic
    private Integer deleted;


    private LocalDateTime createTime;


    private LocalDateTime updateTime;

}