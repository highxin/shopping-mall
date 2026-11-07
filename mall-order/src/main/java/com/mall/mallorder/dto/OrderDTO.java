package com.mall.mallorder.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {

    /** 使用的优惠券ID，可为空 */
    private Long couponId;

    private Long addressId;

}