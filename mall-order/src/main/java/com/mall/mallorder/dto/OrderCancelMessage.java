package com.mall.mallorder.dto;

import lombok.Data;

@Data
public class OrderCancelMessage {
    private Long orderId;
    private Long userId;
}