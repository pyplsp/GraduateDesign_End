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
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/test")
    public String test(HttpServletRequest request){
        System.out.println(request.getAttribute("id"));
        return "成功";
    }

    @PostMapping("/login")
    public Result login(@RequestParam("account") String account, @RequestParam("password") String password) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.ge(User::getAccount, account).ge(User::getPassword,password);
        User user = userService.getOne(queryWrapper);
        if(user != null){
            return Result.success(JwtUtils.createToken(user.getId(), account,password));
        }else{
            return Result.fail("错误");
        }
    }




}
