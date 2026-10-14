package com.mall.malluser.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.exception.BusinessException;
import com.mall.common.utils.JwtUtil;
import com.mall.malluser.dto.UserLoginDTO;
import com.mall.malluser.dto.UserRegisterDTO;
import com.mall.malluser.entity.UserInfo;
import com.mall.malluser.mapper.UserInfoMapper;
import com.mall.malluser.service.UserService;
import com.mall.malluser.vo.LoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserService {

    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public LoginVO login(UserLoginDTO dto) {
        // 1. 查询手机号是否存在
        UserInfo user = this.getOne(
                Wrappers.<UserInfo>lambdaQuery()
                        .eq(UserInfo::getPhone,dto.getPhone())
        );
        if(user == null){
            throw new BusinessException("手机号或密码错误");
        }
        // 2. 校验密码
        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())){
            throw new BusinessException("手机号或密码错误");
        }

        LoginVO loginVO = new LoginVO();
        BeanUtils.copyProperties(user, loginVO);
        // 3. 生成token
        loginVO.setToken(JwtUtil.createToken(user.getId()));
        return loginVO;
    }

    @Override
    public void register(UserRegisterDTO dto) {
        // 1. 查询用户名或手机号是否存在
        UserInfo user = this.getOne(
                Wrappers.<UserInfo>lambdaQuery()
                        .eq(UserInfo::getUsername,dto.getUsername())
                        .or()
                        .eq(UserInfo::getPhone,dto.getPhone())
        );
        if(user != null){
            if(user.getUsername().equals(dto.getUsername())){
                throw new BusinessException("用户名已存在");
            }
            if(user.getPhone().equals(dto.getPhone())){
                throw new BusinessException("手机号已注册");
            }
        }

        //2. 密码加密
        UserInfo newUser = new UserInfo();
        newUser.setUsername(dto.getUsername());
        newUser.setPhone(dto.getPhone());
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        //3. 保存
        this.save(newUser);
    }
}