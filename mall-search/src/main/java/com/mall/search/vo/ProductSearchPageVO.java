package com.mall.search.vo;

import com.mall.search.document.ProductDocument;
import lombok.Data;

import java.util.List;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/10/22
 */
@Data
public class ProductSearchPageVO {
        /**
        * 当前页商品列表
        */
        private List<ProductDocument> records;

        /**
        * 搜索命中总数
        */
        private Long total;

        /**
        * 当前页码
        */
        private Integer pageNum;

        /**
        * 每页数量
        */
        private Integer pageSize;

        /**
        * 总页数
        */
        private Long pages;
}

