package com.mall.mallorder.dto;

import lombok.Data;

@Data
public class OrderCancelMessage {
    private String orderNo;
    private Long userId;
}