package com.chenpeiyu.mqtt.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.chenpeiyu.mqtt.domain.User;
import com.chenpeiyu.mqtt.service.UserService;
import com.chenpeiyu.mqtt.utils.JwtUtils;
import com.chenpeiyu.mqtt.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public Result<Object> login(@RequestParam("account") String account, @RequestParam("password") String password) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(User::getAccount, account).eq(User::getPassword,password);
        User user = userService.getOne(queryWrapper);
        if(user != null){
            return Result.success(JwtUtils.createToken(user.getId(), account,password));
        }else{
            return Result.fail("账号或密码错位");
        }
    }




}
