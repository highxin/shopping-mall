package com.mall.mallorder.vo;

import lombok.Data;

@Data
public class AddressVO {

    private Long userId;

    private String receiver;

    private String phone;

    private String province;

    private String city;

    private String district;

    private String detailAddress;

}