package com.mall.mallorder.mapper;

import com.mall.common.result.Result;
import com.mall.mallorder.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.mallorder.entity.OrderInfo;

import java.util.List;

@Mapper 
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

}





