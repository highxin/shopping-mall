package com.mall.search.controller;

import com.mall.common.result.Result;
import com.mall.search.document.ProductDocument;
import com.mall.search.service.ProductSearchService;
import com.mall.search.vo.ProductSearchPageVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/10/22
 */
@RestController
@RequestMapping("/search/product")
public class ProductSearchController {

    private final ProductSearchService productSearchService;

    public ProductSearchController(ProductSearchService productSearchService) {
        this.productSearchService = productSearchService;
    }

    /**
     * 根据商品名称搜索
     */
    @GetMapping("/list")
    public Result<ProductSearchPageVO> searchByName(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long categoryId) throws IOException {

        ProductSearchPageVO pageVO =
                productSearchService.searchByName(keyword,pageNum,pageSize,minPrice,maxPrice,sortOrder,brandId,categoryId);

        return Result.success(pageVO);
    }
}
