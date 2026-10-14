package com.mall.mallcart.controller;

import com.mall.common.dto.DeleteCartDTO;
import com.mall.common.result.Result;
import com.mall.mallcart.dto.CartAddDTO;
import com.mall.mallcart.dto.CartUpdateDTO;
import com.mall.mallcart.service.CartService;
import com.mall.mallcart.vo.CartItemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/9/25
 */
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public Result<String> addCart(@RequestBody CartAddDTO dto,@RequestHeader Long userId){
        cartService.addCart(dto,userId);
        return Result.success("加入购物车成功");
    }

    @GetMapping("/list")
    public Result<List<CartItemVO>> getCartList(@RequestHeader Long userId){
        return Result.success(cartService.getCartList(userId));
    }

    @PutMapping("/update")
    public Result<String> updateCart(
            @RequestBody CartUpdateDTO dto,
            @RequestHeader Long userId){
        cartService.updateCart(dto,userId);
        return Result.success("修改成功");
    }

    @DeleteMapping("/delete")
    public Result<String> deleteCart(@RequestBody DeleteCartDTO dto, @RequestHeader Long userId){
        cartService.deleteCart(dto.getSkuIds(),userId);
        return Result.success("删除成功");
    }

    @DeleteMapping("/clear")
    public Result<String> clear(@RequestHeader Long userId){
        cartService.clear(userId);
        return Result.success("清空成功");
    }

    @PutMapping("/checked/{skuId}")
    public Result<String> checked(@PathVariable Long skuId,@RequestParam Boolean checked,@RequestHeader Long userId){
        cartService.checked(skuId,checked,userId);
        return Result.success("修改成功");
    }
    @GetMapping("/checked/list")
    public Result<List<CartItemVO>> checkedList(@RequestHeader Long userId){
        return Result.success(cartService.checkedList(userId));
    }
}
