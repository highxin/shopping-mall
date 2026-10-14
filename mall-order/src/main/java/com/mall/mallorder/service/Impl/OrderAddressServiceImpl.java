package com.mall.mallorder.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.mallorder.entity.OrderAddress;
import com.mall.mallorder.mapper.OrderAddressMapper;
import com.mall.mallorder.service.OrderAddressService;
import org.springframework.stereotype.Service;


@Service
public class OrderAddressServiceImpl extends ServiceImpl<OrderAddressMapper, OrderAddress> implements OrderAddressService {


}