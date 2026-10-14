package com.mall.mallorder.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("order_address")
public class OrderAddress {


    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 收货人
     */
    private String receiver;


    /**
     * 手机号
     */
    private String phone;


    /**
     * 省
     */
    private String province;


    /**
     * 市
     */
    private String city;


    /**
     * 区
     */
    private String district;


    /**
     * 详细地址
     */
    private String detailAddress;


    private LocalDateTime createTime;

}