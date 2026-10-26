package com.mall.mallorder.mq;

import com.mall.common.constants.OrderMqConstant;
import com.mall.mallorder.dto.OrderCancelMessage;
import com.mall.mallorder.service.OrderInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "mall.order.mq.consumer-enabled", havingValue = "true", matchIfMissing = true)
@RocketMQMessageListener(
        topic = OrderMqConstant.ORDER_CANCEL_TOPIC,
        consumerGroup = "mall-order-cancel-consumer"
)
public class OrderCancelConsumer implements RocketMQListener<OrderCancelMessage> {

    private final OrderInfoService orderInfoService;

    @Override
    public void onMessage(OrderCancelMessage message) {
        orderInfoService.cancelOrderByTimeout(message.getOrderNo(), message.getUserId());
    }
}