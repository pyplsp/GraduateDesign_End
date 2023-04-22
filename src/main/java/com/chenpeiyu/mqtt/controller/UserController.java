package com.chenpeiyu.mqtt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.chenpeiyu.mqtt.domain.User;
import com.chenpeiyu.mqtt.service.UserService;
import com.chenpeiyu.mqtt.utils.BaseUtils;
import com.chenpeiyu.mqtt.utils.JwtUtils;
import com.chenpeiyu.mqtt.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private BaseUtils baseUtils;
    @Autowired
    UserService userService;

    // 登录
    @PostMapping("/login")
    public Result<Object> login(@RequestBody String json) {
        JSONObject jsonObj = JSON.parseObject(json);
        String account = (String) jsonObj.get("account");
        String password = (String) jsonObj.get("password");
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(User::getAccount, account).eq(User::getPassword,password);
        User user = userService.getOne(queryWrapper);
        if(user != null){
            JSONObject result = new JSONObject();
            result.put("Authorization",JwtUtils.createToken(user.getId(), account, password));
            result.put("Administrator",user.getIfAdministrators());
            result.put("userId",user.getId());
            return Result.success(result);
        }else{
            return Result.fail("账号或密码错误");
        }
    }

    @GetMapping("/unitName")
    public Result<Object> unitName(){
        try {
            if (userService.pySelectUnitName(baseUtils.getIdentity()) != null)
                return Result.success(userService.pySelectUnitName(baseUtils.getIdentity()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.fail("查询错误");
    }

}
