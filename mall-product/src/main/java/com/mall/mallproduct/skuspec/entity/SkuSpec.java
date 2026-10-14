package com.mall.mallproduct.skuspec.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("product_sku_spec")
public class SkuSpec {


    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * SKU ID
     */
    private Long skuId;


    /**
     * 规格ID
     */
    private Long specId;


    /**
     * 规格值ID
     */
    private Long specValueId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}