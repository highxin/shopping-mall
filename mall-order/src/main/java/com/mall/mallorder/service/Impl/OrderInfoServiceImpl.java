package com.mall.mallorder.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.dto.SkuStockDTO;
import com.mall.common.enums.OrderStatusEnum;
import com.mall.common.exception.BusinessException;
import com.mall.common.exception.OrderException;
import com.mall.common.exception.errorcode.OrderErrorCode;
import com.mall.common.result.Result;
import com.mall.common.dto.DeleteCartDTO;
import com.mall.mallorder.dto.OrderDTO;
import com.mall.mallorder.dto.OrderPageDTO;
import com.mall.mallorder.entity.OrderAddress;
import com.mall.mallorder.entity.OrderInfo;
import com.mall.mallorder.entity.OrderItem;
import com.mall.mallorder.feign.AddressFeignClient;
import com.mall.mallorder.feign.CartFeignClient;
import com.mall.mallorder.feign.ProductFeignClient;
import com.mall.mallorder.mapper.OrderInfoMapper;
import com.mall.mallorder.service.OrderAddressService;
import com.mall.mallorder.service.OrderInfoService;
import com.mall.mallorder.service.OrderItemService;
import com.mall.mallorder.transition.OrderStatusTransition;
import com.mall.mallorder.vo.*;
import lombok.RequiredArgsConstructor;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.util.RandomUtil;


@Service
@RequiredArgsConstructor
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    private final OrderItemService orderItemService;
    private final OrderAddressService orderAddressService;
    private final AddressFeignClient addressFeignClient;
    private final CartFeignClient cartFeignClient;
    private final ProductFeignClient productFeignClient;

    @GlobalTransactional(
            name = "create-order-tx",
            rollbackFor = Exception.class,
            timeoutMills = 300000
    )
    @Override
    public String createOrder(OrderDTO orderDTO,Long userId){

        //1.生成订单号
        String orderNo =
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                        + RandomUtil.randomNumbers(6);

        // 2.查询订单地址
        AddressVO address = getAddress(orderDTO.getAddressId());
        // 3.创建订单主表
        OrderInfo order=new OrderInfo();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setStatus(0);
        BigDecimal totalAmount=BigDecimal.ZERO;
        // 4.查询选中的购物车商品
        List<CartItemVO> cartItems = getCartItems(userId);
        if(cartItems.isEmpty()){
            throw new OrderException(OrderErrorCode.CART_ITEM_EMPTY);
        }
        // 5.扣减库存
        List<SkuStockDTO> skuStockDTOs = buildSkuStockDTO(cartItems);
        deductStock(skuStockDTOs);

        for(CartItemVO cartItem:cartItems){
            //计算订单总金额
            BigDecimal itemAmount=
                    cartItem.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(cartItem.getQuantity())
                            );
            totalAmount=totalAmount.add(itemAmount);
        }
        // 5.保存订单总金额
        order.setTotalAmount(totalAmount);
        save(order);
        // 6.保存订单地址
        saveOrderAddress(order,address,userId);

        // 7.保存订单商品
        List<Long> skuIds = saveOrderItems(order, cartItems);
        if (skuIds.isEmpty()) {
            throw new OrderException(OrderErrorCode.ORDER_ITEM_SKU_IDS_EMPTY);
        }

        // 9.删除购物车商品
            DeleteCartDTO dto = new DeleteCartDTO();
            dto.setSkuIds(skuIds);
            cartFeignClient.deleteCart(dto, userId);
        // 10.返回订单号
        return orderNo;

    }

    @Override
    public OrderVO getOrderDetail(String orderId,Long userId) {
        // 根据订单id和用户id查询订单主表
        OrderInfo orderInfo = lambdaQuery()
                .eq(OrderInfo::getId, orderId)
                .eq(OrderInfo::getUserId, userId)
                .one();
        if(orderInfo==null){
            throw new OrderException(OrderErrorCode.ORDER_NOT_FOUND);
        }
        // 查询订单商品
        List<OrderItem> orderItems = orderItemService.list(
                new LambdaQueryWrapper<OrderItem>()
                        .eq(OrderItem::getOrderId, orderId)
        );
        if(orderItems.isEmpty()){
            throw new OrderException(OrderErrorCode.ORDER_ITEM_NOT_FOUND);
        }
        // 获取订单地址

        // 查询订单地址
        OrderAddress orderAddress = orderAddressService.lambdaQuery()
                .eq(OrderAddress::getOrderId, orderId)
                .one();
        // 转换为VO
        OrderVO orderVO = new OrderVO();
        List<OrderItemVO> voList = new ArrayList<>();
        // 转换订单商品为VO
        for(OrderItem orderItem:orderItems){
            OrderItemVO vo = new OrderItemVO();
            BeanUtils.copyProperties(orderItem,vo);
            voList.add(vo);
        }
        // 转换订单地址为VO
        AddressVO address = new AddressVO();
        if (orderAddress != null) {
            BeanUtils.copyProperties(orderAddress,address);
        }

        BeanUtils.copyProperties(orderInfo,orderVO);
        orderVO.setItems(voList);
        orderVO.setAddress(address);
        return orderVO;
    }

    @Override
    public IPage<OrderInfoVO> page(OrderPageDTO dto, Long userId) {

        Page<OrderInfo> page = new Page<>(dto.getPageNo(), dto.getPageSize());
        // 分页查询订单
        IPage<OrderInfo> Page;
        if (dto.getOrderStatus() != null) {
            Page = lambdaQuery()
                    .eq(OrderInfo::getStatus, dto.getOrderStatus())
                    .eq(OrderInfo::getUserId, userId)
                    .orderByDesc(OrderInfo::getId)
                    .page(page);

        } else {
            Page = lambdaQuery()
                    .eq(OrderInfo::getUserId, userId)
                    .orderByDesc(OrderInfo::getId)
                    .page(page);
        }
        // 转换为VO
        return Page.convert(order -> {
            OrderInfoVO vo = new OrderInfoVO();
            BeanUtils.copyProperties(order, vo);
            return vo;
        });
    }

    @Override
    public Integer getOrderStatus(String orderId, Long userId) {
        // 根据订单id和用户id查询订单主表
        OrderInfo orderInfo = lambdaQuery()
                .eq(OrderInfo::getId, orderId)
                .eq(OrderInfo::getUserId, userId)
                .one();
        if(orderInfo==null){
            throw new OrderException(OrderErrorCode.ORDER_NOT_FOUND);
        }
        return orderInfo.getStatus();
    }



    @Override
    @GlobalTransactional(name = "cancel-order-tx",
            rollbackFor = Exception.class,
            timeoutMills = 300000)
    public String cancelOrder(String orderId, Long userId) {
        //1.查询订单并校验归属
        OrderInfo orderInfo = lambdaQuery()
                .eq(OrderInfo::getId, orderId)
                .eq(OrderInfo::getUserId, userId)
                .one();
        if (orderInfo == null) {
            throw new OrderException(OrderErrorCode.ORDER_NOT_FOUND);
        }
        //2.校验订单状态
        Integer status = orderInfo.getStatus();
        if (!OrderStatusTransition.canTransition(status, OrderStatusEnum.CANCELLED.getCode())) {
            throw new OrderException(OrderErrorCode.ORDER_STATUS_ERROR);
        }
        //3.回补库存(创建订单时已扣减,取消需归还)
        restoreStock(orderId);
        //4.更新订单状态为已取消
        lambdaUpdate()
                .eq(OrderInfo::getId, orderInfo.getId())
                .set(OrderInfo::getStatus, OrderStatusEnum.CANCELLED.getCode())
                .update();
        return orderInfo.getOrderNo()+"取消成功";
    }

    // 回补库存:调用商品服务专门的加库存接口
    private void restoreStock(String orderId) {
        List<OrderItem> orderItems = orderItemService.list(
                new LambdaQueryWrapper<OrderItem>()
                        .eq(OrderItem::getOrderId, orderId)
        );
        if (orderItems.isEmpty()) {
            return;
        }
        List<SkuStockDTO> skuStockDTOs = new ArrayList<>();
        for (OrderItem item : orderItems) {
            SkuStockDTO dto = new SkuStockDTO();
            dto.setSkuId(item.getSkuId());
            dto.setQuantity(item.getQuantity());
            skuStockDTOs.add(dto);
        }
        Result<String> stockResult = productFeignClient.addStock(skuStockDTOs);
        if (stockResult.getCode() != 200) {
            throw new BusinessException(stockResult.getMsg());
        }
    }

    @Override
    public String payOrder(String orderId, Long userId) {
        return updateStatus(
                orderId,
                userId,
                OrderStatusEnum.PAID.getCode()
        );
    }

    @Override
    public String receiveOrder(String orderId, Long userId) {
        return updateStatus(
                orderId,
                userId,
                OrderStatusEnum.RECEIVED.getCode()
        );
    }

    @Override
    public String updateStatus(String orderId, Long userId, Integer newStatus){
        OrderInfo orderInfo = lambdaQuery()
                        .eq(OrderInfo::getId,orderId)
                        .eq(OrderInfo::getUserId,userId)
                        .one();
        if(orderInfo==null){
            throw new OrderException(OrderErrorCode.ORDER_NOT_FOUND);
        }
        Integer oldStatus = orderInfo.getStatus();
        if(!OrderStatusTransition.canTransition(oldStatus, newStatus)){
            throw new OrderException(OrderErrorCode.ORDER_STATUS_ERROR);
        }
        boolean update = lambdaUpdate()
                        .eq(OrderInfo::getId,orderId)
                        .eq(OrderInfo::getStatus,oldStatus)
                        .set(OrderInfo::getStatus,newStatus)
                        .update();
        if(!update){
            throw new OrderException(OrderErrorCode.ORDER_STATUS_ERROR);
        }
        return orderInfo.getOrderNo();
    }

    // 查询购物车方法
    private List<CartItemVO> getCartItems(Long userId){
        Result<List<CartItemVO>> cartListResult=cartFeignClient.checkedList(userId);
        if(cartListResult.getCode()!=200){
            throw new BusinessException(cartListResult.getMsg());
        }
        return cartListResult.getData();
    }

    // 查询地址方法
    private AddressVO getAddress(Long addressId){
        Result<AddressVO> addressResult=addressFeignClient.get(addressId);
        if(addressResult.getCode()!=200){
            throw new BusinessException(addressResult.getMsg());
        }
        if(addressResult.getData()==null){
            throw new OrderException(OrderErrorCode.ORDER_ADDRESS_NOT_FOUND);
        }
        return addressResult.getData();
    }

    // 构造Sku库存DTO
    private List<SkuStockDTO> buildSkuStockDTO(List<CartItemVO> cartItems){
        List<SkuStockDTO> list = new ArrayList<>();
        for(CartItemVO cartItem:cartItems){
            SkuStockDTO dto = new SkuStockDTO();
            dto.setSkuId(cartItem.getSkuId());
            dto.setQuantity(cartItem.getQuantity());
            list.add(dto);
        }
        return list;
    }

    // 扣减库存方法
    private void deductStock(List<SkuStockDTO> list){
        Result<String> stockResult= productFeignClient.deductStock(list);
        if(stockResult.getCode()!=200){
            throw new BusinessException(stockResult.getMsg());
        }
    }

    // 保存订单地址方法
    private void saveOrderAddress(OrderInfo order,AddressVO address,Long userId){
        OrderAddress orderAddress=new OrderAddress();
        BeanUtils.copyProperties(address,orderAddress);
        orderAddress.setId(null);
        orderAddress.setOrderId(order.getId());
        orderAddress.setUserId(userId);
        orderAddressService.save(orderAddress);
    }

    // 保存订单商品方法
    private List<Long> saveOrderItems(OrderInfo order,List<CartItemVO> cartItems){
        List<OrderItem> orderItems=new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        for(CartItemVO cartItem:cartItems){
            OrderItem item=new OrderItem();
            skuIds.add(cartItem.getSkuId()); // 收集选中的购物车商品Id,用于删除购物车商品
            item.setOrderId(order.getId());
            BeanUtils.copyProperties(cartItem,item);
            item.setTotalAmount(
                    cartItem.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(cartItem.getQuantity())
                            )
            );
            orderItems.add(item);// 收集订单商品,用于批量保存

        }
        // 批量保存订单商品
        orderItemService.saveBatch(orderItems);
        // 返回选中的购物车商品Id
        return skuIds;
    }

}