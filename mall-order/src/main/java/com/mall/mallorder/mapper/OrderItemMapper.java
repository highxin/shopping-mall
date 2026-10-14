package com.mall.mallorder.mapper;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.mallorder.entity.OrderItem;


@Mapper 
public interface OrderItemMapper extends BaseMapper<OrderItem>{

}