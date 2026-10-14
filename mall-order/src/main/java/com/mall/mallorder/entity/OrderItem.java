package com.mall.mallorder.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@TableName("order_item")
public class OrderItem {


    /**
     * 订单商品ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 订单ID
     */
    private Long orderId;


    /**
     * SKU ID
     */
    private Long skuId;


    /**
     * 商品名称快照
     */
    private String skuName;


    /**
     * 商品购买价格
     */
    private BigDecimal price;


    /**
     * 购买数量
     */
    private Integer quantity;


    /**
     * 商品总金额
     */
    private BigDecimal totalAmount;


    private LocalDateTime createTime;


    private LocalDateTime updateTime;


    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;

}