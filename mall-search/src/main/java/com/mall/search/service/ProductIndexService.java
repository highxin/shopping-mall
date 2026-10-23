package com.mall.search.service;

import cn.hutool.core.bean.BeanUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import com.mall.common.dto.ProductIndexDTO;
import com.mall.common.result.Result;
import com.mall.search.document.ProductDocument;
import com.mall.search.feign.ProductFeignClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 * 商品索引服务
 * @author highxin
 * @version 1.0.0
 * @since 2026/10/21
 */
@Service
public class ProductIndexService {
    private final ProductFeignClient productFeignClient;

    private final ElasticsearchClient elasticsearchClient;

    public ProductIndexService(ProductFeignClient productFeignClient, ElasticsearchClient elasticsearchClient) {
        this.productFeignClient = productFeignClient;
        this.elasticsearchClient = elasticsearchClient;
    }

    /**
     * 同步商品数据到 Elasticsearch
     */
    public void syncProductIndex() throws IOException {

        // 1. 通过 Feign 查询商品服务
        Result<List<ProductIndexDTO>> result =
                productFeignClient.listForSearch();

        if (result == null || result.getData() == null) {
            throw new IllegalStateException("查询商品索引数据失败");
        }

        List<ProductIndexDTO> productList = result.getData();

        if (productList.isEmpty()) {
            return;
        }

        // 2. 将 DTO 转换为 Elasticsearch 文档
        List<BulkOperation> operations = productList.stream()
                .map(dto -> {
                    ProductDocument document = new ProductDocument();

                    BeanUtil.copyProperties(dto, document);

                    return BulkOperation.of(op -> op
                            .index(idx -> idx
                                    .index("mall_product")
                                    .id(String.valueOf(document.getId()))
                                    .document(document)
                            )
                    );
                })
                .toList();

        // 3. 批量写入 Elasticsearch
        BulkResponse response = elasticsearchClient.bulk(
                BulkRequest.of(request -> request.operations(operations))
        );

        // 4. 检查批量写入结果
        if (response.errors()) {
            throw new IllegalStateException("部分商品写入 Elasticsearch 失败");
        }
    }
}