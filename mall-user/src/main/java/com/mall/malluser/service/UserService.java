package com.mall.malluser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.malluser.dto.UserLoginDTO;
import com.mall.malluser.dto.UserRegisterDTO;
import com.mall.malluser.entity.UserInfo;
import com.mall.malluser.vo.LoginVO;


public interface UserService extends IService<UserInfo> {
    /**
     * 注册
     * @param dto 注册DTO
     */
    void register(UserRegisterDTO dto);

    /**
     * 登录
     * @param dto 登录DTO
     * @return 登录成功后的用户信息
     */
    LoginVO login(UserLoginDTO dto);

}