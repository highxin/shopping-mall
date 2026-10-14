package com.mall.mallproduct.spu.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SpuVO {

    private Long id;

    private String name;

    private Long brandId;

    private Long categoryId;

    private String description;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}