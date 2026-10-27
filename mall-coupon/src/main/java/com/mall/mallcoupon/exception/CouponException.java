package com.mall.mallcoupon.exception;

import com.mall.common.exception.BusinessException;
import com.mall.common.exception.ErrorCode;
import com.mall.mallcoupon.enums.CouponErrorCode;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 * @author highxin
 * @version 1.0.0
 * @since 2026/10/27
 */
public class CouponException extends BusinessException {
  public CouponException(String message) {
    super(message);
  }
  public CouponException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }
  public CouponException(CouponErrorCode errorCode) {
    super(errorCode);
  }
}
