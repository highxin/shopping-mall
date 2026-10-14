package com.mall.malluser.controller;

import com.mall.common.result.Result;
import com.mall.malluser.entity.UserAddress;
import com.mall.malluser.service.UserAddressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user/address")
public class UserAddressController {


    private final UserAddressService userAddressService;


    public UserAddressController(UserAddressService userAddressService){
        this.userAddressService=userAddressService;
    }



    @PostMapping("/save")
    public Result<String> save(@RequestBody UserAddress address){
        userAddressService.save(address);
        return Result.success("保存成功");
    }

    @GetMapping("/list/{userId}")
    public Result<List<UserAddress>> list(@PathVariable Long userId){
        return Result.success(
                userAddressService.lambdaQuery()
                        .eq(UserAddress::getUserId,userId)
                        .list()
        );
    }

    @GetMapping("/{id}")
    public Result<UserAddress> get(@PathVariable Long id){
        return Result.success(userAddressService.getAddress(id));
    }

    @PutMapping("/update")
    public Result<String> update(@RequestBody UserAddress address){
        userAddressService.updateById(address);
        return Result.success("修改成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id){
        userAddressService.removeById(id);
        return Result.success("删除成功");
    }


    @PutMapping("/default/{id}")
    public Result<String> setDefault(@PathVariable Long id){
        userAddressService.setDefault(id);
        return Result.success("设置默认成功");
    }
}