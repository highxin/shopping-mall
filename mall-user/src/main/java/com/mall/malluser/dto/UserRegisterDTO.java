package com.mall.malluser.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class UserRegisterDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "密码不能为空")
    private String password;

}