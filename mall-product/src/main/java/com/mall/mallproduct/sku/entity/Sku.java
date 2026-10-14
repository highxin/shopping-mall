package com.mall.mallproduct.sku.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product_sku")
public class Sku {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属SPU
     */
    private Long spuId;


    /**
     * SKU名称
     */
    private String name;


    /**
     * 价格
     */
    private BigDecimal price;


    /**
     * 库存
     */
    private Integer stock;


    /**
     * 状态
     */
    private Integer status;

    /**
     * SKU规格哈希值
     */
    private String specHash;


    private LocalDateTime createTime;


    private LocalDateTime updateTime;


    @TableLogic
    private Integer deleted;
}