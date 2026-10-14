package com.mall.mallcart.service;


import com.mall.mallcart.dto.CartAddDTO;
import com.mall.mallcart.dto.CartUpdateDTO;
import com.mall.mallcart.vo.CartItemVO;

import java.util.List;


public interface CartService {

    /**
     * 添加sku到购物车
     * @param dto 包含skuId,商品数量
     */
    void addCart(CartAddDTO dto,Long userId);

    /**
     * 获取购物车消息
     * @return 购物车消息
     */
    List<CartItemVO> getCartList(Long userId);

    /**
     * 更新购物车商品数量
     * @param dto 包含skuId,商品数量
     */
    void updateCart(CartUpdateDTO dto,Long userId);

    /**
     * 删除选中购物车商品
     * @param skuIds skuIds
     */
    void deleteCart(List<Long> skuIds,Long userId);

    /**
     * 清空购物车商品
     * @param userId 用户Id
     */
    void clear(Long userId);

    /**
     * 选中购物车商品
     * @param skuId skuId
     * @param checked 是否选中
     */
    void checked(Long skuId,Boolean checked,Long userId);

    /**
     * 获取选中的购物车商品
     * @param userId 用户Id
     * @return 选中的购物车商品
     */
    List<CartItemVO> checkedList(Long userId);


    
}