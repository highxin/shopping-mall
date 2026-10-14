package com.mall.common.result;

import lombok.Data;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/8/22
 */
@Data
public class Result <T> {
    private Integer code;
    private String msg;
    private T data;

    public Result() {

    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(ErrorCode.SYSTEM_ERROR.getCode(), msg, null);
    }

    public static <T> Result<T> error(ErrorCode errorCode){
        Result<T> result = new Result<>();
        result.setCode(errorCode.getCode());
        result.setMsg(errorCode.getMessage());
        return result;
    }

    public static <T> Result<T> error(Integer code, String message){
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(message);
        return result;
    }

}
