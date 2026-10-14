package com.mall.common.exception.errorcode;

import com.mall.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/10/10
 */
@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    SUCCESS(200, "操作成功"),
    PARAM_ERROR(400, "参数错误"),
    BUSINESS_ERROR(500, "业务异常"),
    SYSTEM_ERROR(501, "系统异常"),
    UNKNOWN_ERROR(502, "未知异常"),
    DUPLICATE_KEY_ERROR(503, "重复键异常"),
    ;
    private final Integer code;
    private final String message;
}
