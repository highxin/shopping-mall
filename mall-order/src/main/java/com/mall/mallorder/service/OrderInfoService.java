package com.mall.mallorder.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.result.Result;
import com.mall.mallorder.dto.OrderDTO;
import com.mall.mallorder.dto.OrderPageDTO;
import com.mall.mallorder.entity.OrderInfo;
import com.mall.mallorder.vo.OrderInfoVO;
import com.mall.mallorder.vo.OrderVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface OrderInfoService extends IService<OrderInfo>{

    /**
     * 创建订单
     * @param orderDTO 订单DTO
     * @return 订单编号
     */
    String createOrder(OrderDTO orderDTO,Long userId);

    /**
     * 获取订单详情
     * @param orderId 订单编号
     * @return 订单详情
     */
    OrderVO getOrderDetail(String orderId,Long userId);


    /**
     * 分页查询订单列表
     * @param dto 订单分页DTO
     * @param userId 用户id
     * @return 订单分页列表
     */
    IPage<OrderInfoVO> page(OrderPageDTO dto, Long userId);

    /**
     * 取消订单
     * @param orderId 订单编号
     * @param userId 用户id
     * @return 操作结果
     */
    String cancelOrder(String orderId, Long userId);

    /**
     * 获取订单状态
     * @param orderId 订单编号
     * @param userId 用户id
     * @return 订单状态
     */
    Integer getOrderStatus(String orderId, Long userId);

    /**
     * 模拟支付
     * @param orderId 订单编号
     * @param userId 用户id
     * @return 操作结果
     */
    String payOrder(String orderId, Long userId);

    /**
     * 收货
     * @param orderId 订单编号
     * @param userId 用户id
     * @return 操作结果
     */
    String receiveOrder(String orderId, Long userId);

    /**
     * 更新订单状态
     * @param orderId 订单编号
     * @param userId 用户id
     * @param newStatus 新状态
     * @return 操作结果
     */
    String updateStatus(String orderId, Long userId, Integer newStatus);
}