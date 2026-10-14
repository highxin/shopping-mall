package com.mall.common.result;


import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum ErrorCode {

    SUCCESS(200,"操作成功"),
    PARAM_ERROR(400,"参数错误"),
    BUSINESS_ERROR(500,"业务异常"),
    SYSTEM_ERROR(501,"系统异常"),
    UNKNOWN_ERROR(502,"未知异常");

    private final Integer code;
    private final String message;

}