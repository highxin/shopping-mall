
package com.mall.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.mall.search.document.ProductDocument;
import com.mall.search.vo.ProductSearchPageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 * 搜索商品服务
 * @author highxin
 * @version 1.0.0
 * @since 2026/10/22
 */
@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ElasticsearchClient client;
    /**
     * 根据商品名称描述和价格范围搜索商品
     */
    public ProductSearchPageVO searchByName(String keyword, int pageNum, int pageSize, BigDecimal minPrice, BigDecimal maxPrice, String sortOrder,Long brandId,Long categoryId)
            throws IOException {

        SearchResponse<ProductDocument> response =
                client.search(s -> s
                                .index("mall_product")
                                .from((pageNum - 1) * pageSize)
                                .size(pageSize)
                                .sort(sort -> {
                                    if ("asc".equalsIgnoreCase(sortOrder)) {
                                        sort.field(f -> f
                                                .field("minPrice")
                                                .order(co.elastic.clients.elasticsearch._types.SortOrder.Asc)
                                        );
                                    } else if ("desc".equalsIgnoreCase(sortOrder)) {
                                        sort.field(f -> f
                                                .field("minPrice")
                                                .order(co.elastic.clients.elasticsearch._types.SortOrder.Desc)
                                        );
                                    } else {
                                        sort.score(q -> q.order(
                                                co.elastic.clients.elasticsearch._types.SortOrder.Desc
                                        ));
                                    }
                                    return sort;
                                })
                                .query(q -> q
                                        .bool(b -> {
                                            b.must(m -> m
                                                    .multiMatch(mm -> mm
                                                            .fields("name", "description")
                                                            .query(keyword)
                                                    )
                                            );
                                            if (minPrice != null) {
                                                b.filter(f -> f
                                                        .range(r -> r
                                                                .number(n -> n
                                                                        .field("maxPrice")
                                                                        .gte(minPrice.doubleValue())
                                                                )
                                                        )
                                                );
                                            }
                                            if (maxPrice != null) {
                                                b.filter(f -> f
                                                        .range(r -> r
                                                                .number(n -> n
                                                                        .field("minPrice")
                                                                        .lte(maxPrice.doubleValue())
                                                                )
                                                        )
                                                );
                                            }
                                            if (brandId != null) {
                                                b.filter(f -> f
                                                        .term(t -> t
                                                                .field("brandId")
                                                                .value(brandId)
                                                        )
                                                );
                                            }
                                            if (categoryId != null) {
                                                b.filter(f -> f
                                                        .term(t -> t
                                                                .field("categoryId")
                                                                .value(categoryId)
                                                        )
                                                );
                                            }
                                            return b;
                                        })
                                ),
                        ProductDocument.class
                );

        List<ProductDocument> records = response.hits().hits().stream()
                .map(hit -> hit.source())
                .filter(document -> document != null)
                .toList();
        long total = response.hits().total() == null
                ? 0L
                : response.hits().total().value();

        ProductSearchPageVO pageVO = new ProductSearchPageVO();
        pageVO.setRecords(records);
        pageVO.setTotal(total);
        pageVO.setPageNum(pageNum);
        pageVO.setPageSize(pageSize);
        pageVO.setPages((total + pageSize - 1) / pageSize);

        return pageVO;
    }
}
