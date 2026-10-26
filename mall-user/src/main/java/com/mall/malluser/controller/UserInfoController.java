package com.mall.malluser.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall.common.result.Result;
import com.mall.malluser.dto.UserLoginDTO;
import com.mall.malluser.dto.UserRegisterDTO;
import com.mall.malluser.service.UserService;
import com.mall.malluser.vo.LoginVO;
import com.mall.malluser.vo.UserVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserService userService;

    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody UserRegisterDTO dto){
        userService.register(dto);
        return Result.success("注册成功");
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody UserLoginDTO dto) {
        LoginVO loginVO = userService.login(dto);
        return Result.success(loginVO);
    }

    @GetMapping("/page")
    public Result<IPage<UserVO>> page(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String keyword) {
        return Result.success(userService.pageUser(pageNum, pageSize, keyword));
    }

    @PutMapping("/updateStatus")
    public Result<String> updateStatus(@RequestBody Map<String, Object> body) {
        Long id = Long.valueOf(body.get("id").toString());
        Integer status = Integer.valueOf(body.get("status").toString());
        userService.updateStatus(id, status);
        return Result.success("更新成功");
    }

}