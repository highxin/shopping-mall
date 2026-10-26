package com.mall.malluser.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.exception.BusinessException;
import com.mall.common.utils.JwtUtil;
import com.mall.malluser.dto.UserLoginDTO;
import com.mall.malluser.dto.UserRegisterDTO;
import com.mall.malluser.entity.UserInfo;
import com.mall.malluser.mapper.UserInfoMapper;
import com.mall.malluser.service.UserService;
import com.mall.malluser.vo.LoginVO;
import com.mall.malluser.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


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

    @Override
    public IPage<UserVO> pageUser(Long pageNum, Long pageSize, String keyword) {
        Page<UserInfo> page = this.lambdaQuery()
                .and(StringUtils.hasText(keyword), w -> w
                        .like(UserInfo::getUsername, keyword)
                        .or()
                        .like(UserInfo::getPhone, keyword))
                .orderByDesc(UserInfo::getCreateTime)
                .page(new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize));

        // 实体转 VO，清除密码
        return page.convert(user -> {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(user, vo);
            return vo;
        });
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        UserInfo user = this.getById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        this.update(Wrappers.<UserInfo>lambdaUpdate()
                .eq(UserInfo::getId, id)
                .set(UserInfo::getStatus, status));
    }
}