package com.mall.mallorder.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall.common.result.Result;
import com.mall.mallorder.dto.OrderDTO;
import com.mall.mallorder.dto.OrderPageDTO;
import com.mall.mallorder.service.OrderInfoService;
import com.mall.mallorder.vo.OrderInfoVO;
import com.mall.mallorder.vo.OrderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {


    private final OrderInfoService orderService;


    @PostMapping("/create")
    public Result<String> createOrder(@RequestBody OrderDTO orderDTO,@RequestHeader Long userId){
        String orderId = orderService.createOrder(orderDTO,userId);
        return Result.success(orderId);
    }

    @GetMapping("/detail/{orderId}")
    public Result<OrderVO> getOrderDetail(@PathVariable String orderId,@RequestHeader Long userId){
        OrderVO orderVO = orderService.getOrderDetail(orderId,userId);
        return Result.success(orderVO);
    }

    @GetMapping("/list")
    public Result<IPage<OrderInfoVO>> getOrderList(OrderPageDTO orderPageDTO, @RequestHeader Long userId){
        IPage<OrderInfoVO> voPage = orderService.page(orderPageDTO, userId);
        return Result.success(voPage);
    }

    @PostMapping("/cancel/{orderId}")
    public Result<String> cancelOrder(@PathVariable String orderId,@RequestHeader Long userId){
        String msg = orderService.cancelOrder(orderId,userId);
        return Result.success(msg);
    }

    @GetMapping("/status/{orderId}")
    public Result<Integer> getOrderStatus(@PathVariable String orderId,@RequestHeader Long userId){
        Integer status = orderService.getOrderStatus(orderId,userId);
        return Result.success(status);
    }

    @PostMapping("/pay/{orderId}")
    public Result<String> payOrder(@PathVariable String orderId, @RequestHeader Long userId){
        String msg = orderService.payOrder(orderId, userId);
        return Result.success(msg);
    }

    @PostMapping("/receive/{orderId}")
    public Result<String> receiveOrder(@PathVariable String orderId, @RequestHeader Long userId) {
        return Result.success(orderService.receiveOrder(orderId, userId));
    }

}