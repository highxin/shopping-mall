package com.mall.search.controller;

import com.mall.common.result.Result;
import com.mall.search.service.ProductIndexService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/10/21
 */
@RestController
@RequestMapping("/search")
public class ProductIndexController {
    private final ProductIndexService productIndexService;

    public ProductIndexController(ProductIndexService productIndexService) {
        this.productIndexService = productIndexService;
    }
    /**
     * 手动同步商品索引
     */
    @PostMapping("/sync")
    public Result<String> syncProductIndex() throws IOException {
        productIndexService.syncProductIndex();
        return Result.success("商品索引同步成功");
    }
}
