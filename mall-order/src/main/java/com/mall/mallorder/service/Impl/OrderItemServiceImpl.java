package com.mall.mallorder.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.mallorder.entity.OrderItem;
import com.mall.mallorder.mapper.OrderItemMapper;
import com.mall.mallorder.service.OrderItemService;
import org.springframework.stereotype.Service;


@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {


}