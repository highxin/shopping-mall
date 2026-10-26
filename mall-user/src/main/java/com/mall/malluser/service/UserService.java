package com.mall.malluser.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.malluser.dto.UserLoginDTO;
import com.mall.malluser.dto.UserRegisterDTO;
import com.mall.malluser.entity.UserInfo;
import com.mall.malluser.vo.LoginVO;
import com.mall.malluser.vo.UserVO;


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

    /**
     * 用户分页列表
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param keyword 关键词（用户名/手机号模糊）
     * @return 分页结果（不含密码）
     */
    IPage<UserVO> pageUser(Long pageNum, Long pageSize, String keyword);

    /**
     * 更新用户状态（启用/禁用）
     * @param id 用户ID
     * @param status 状态
     */
    void updateStatus(Long id, Integer status);

}