package com.mall.mallorder.feign;

import com.mall.common.result.Result;
import com.mall.common.dto.DeleteCartDTO;
import com.mall.mallorder.vo.CartItemVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/10/4
 */
@FeignClient(name = "mall-cart")
public interface CartFeignClient {

    @GetMapping("/cart/checked/list")
    Result<List<CartItemVO>> checkedList(
            @RequestHeader Long userId
    );

    @DeleteMapping("/cart/delete")
    Result<String> deleteCart(
            @RequestBody DeleteCartDTO dto,
            @RequestHeader Long userId
    );
}
