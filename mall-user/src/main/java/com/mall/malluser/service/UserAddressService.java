package com.mall.malluser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.malluser.entity.UserAddress;

public interface UserAddressService extends IService<UserAddress> {

    /**
     * 设置默认地址
     * @param id 地址id
     */
    void setDefault(Long id);

    /**
     * 获取地址详情
     * @param id 地址id
     * @return 地址详情
     */
    UserAddress getAddress(Long id);
}