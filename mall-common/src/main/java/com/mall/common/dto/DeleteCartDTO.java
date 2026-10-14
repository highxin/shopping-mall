package com.mall.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeleteCartDTO {

    private List<Long> skuIds;

}