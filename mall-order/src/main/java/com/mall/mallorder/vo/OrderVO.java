package com.mall.mallorder.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderVO {

    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单金额
     */
    private BigDecimal totalAmount;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    /**
     * 商品列表
     */
    private List<OrderItemVO> items;


    /**
     * 地址
     */
    private AddressVO address;

}