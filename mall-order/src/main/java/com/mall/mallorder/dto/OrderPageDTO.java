package com.mall.mallorder.dto;

import lombok.Data;

@Data
public class OrderPageDTO {

    private Integer pageNo=1;

    private Integer pageSize=10;

    private Integer orderStatus;

}