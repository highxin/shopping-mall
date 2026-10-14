package com.mall.malluser.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.malluser.entity.UserAddress;
import com.mall.malluser.mapper.UserAddressMapper;
import com.mall.malluser.service.UserAddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    @Override
    @Transactional
    public void setDefault(Long id){
        //1.查询当前地址
        UserAddress address=getById(id);
        if(address==null){
            throw new RuntimeException("地址不存在");
        }
        //2.取消该用户之前默认地址
        lambdaUpdate()
                .eq(UserAddress::getUserId,address.getUserId())
                .set(UserAddress::getIsDefault,0)
                .update();
        //3.设置当前地址默认
        address.setIsDefault(1);
        updateById(address);
    }

    @Override
    public UserAddress getAddress(Long id) {
        UserAddress userAddress = getById(id);
        if(userAddress==null){
            throw new RuntimeException("地址不存在");
        }
        return userAddress;
    }
}