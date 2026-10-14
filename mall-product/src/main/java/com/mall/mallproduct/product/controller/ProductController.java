package com.mall.mallproduct.product.controller;

import com.mall.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/8/16
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/test")
    public Result<String> test() {
        return Result.success("success!");
    }
}
