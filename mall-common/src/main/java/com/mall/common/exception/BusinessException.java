package com.mall.common.exception;

import lombok.Getter;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/8/22
 */
@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;

    public BusinessException(String msg) {
        super(msg);
        this.code = 500;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String msg) {
        super(msg);
        this.code = errorCode.getCode();
    }

}
