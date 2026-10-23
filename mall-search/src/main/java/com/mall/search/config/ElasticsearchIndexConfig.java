package com.mall.search.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class ElasticsearchIndexConfig {

    private final ElasticsearchClient client;

    /**
     * 创建商品索引
     */
    @PostConstruct
    public void createProductIndex() throws IOException {

        String indexName = "mall_product";

        // 判断索引是否已经存在
        boolean exists = client.indices()
                .exists(ExistsRequest.of(e -> e.index(indexName)))
                .value();

        if (exists) {
            System.out.println("商品索引已存在：" + indexName);
            return;
        }

        // 创建索引并定义字段映射
        client.indices().create(CreateIndexRequest.of(c -> c
                .index(indexName)
                .mappings(m -> m
                        .properties("id", p -> p.long_(v -> v))
                        .properties("name", p -> p.text(v -> v))
                        .properties("brandId", p -> p.long_(v -> v))
                        .properties("categoryId", p -> p.long_(v -> v))
                        .properties("description", p -> p.text(v -> v))
                        .properties("status", p -> p.integer(v -> v))
                        .properties("minPrice", p -> p.double_(v -> v))
                        .properties("maxPrice", p -> p.double_(v -> v))
                        .properties("totalStock", p -> p.integer(v -> v))
                )
        ));

        System.out.println("商品索引创建成功：" + indexName);
    }
}