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
            result.put("userId",user.getId());
            return Result.success(result);
        }else{
            return Result.fail("账号或密码错误");
        }
    }

    // 分页获取用户列表，只有主账户可以获取
    @GetMapping("/list")
    public Result<Object> selectUsers(
            @RequestParam("size") Integer size,
            @RequestParam("current") Integer current
    ) {
        if (baseUtils.getIdentity() == 1){
            return Result.success(userService.pySelectUsers(size,current));
        }
        return Result.fail("无权限");
    }

    // 获取单个用户信息
    @GetMapping("/{userId}")
    public Result<Object> selectOne(@PathVariable Integer userId){
        return Result.success(userService.getById(userId));
    }
    // 获取所有的用户单位，除了主账户
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

    // 修改密码
    @PostMapping("/psc")
    public Result<Object> changePassword(@RequestBody String json){
        try {
            JSONObject jsonObj = JSON.parseObject(json);
            User user = new User();
            user.setId(baseUtils.getIdentity());
            user.setPassword((String) jsonObj.get("password"));
            userService.updateById(user);
            return Result.success("密码修改成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.fail("密码修改失败");
    }

    // 添加用户
    @PostMapping
    public Result<Object> addUser(@RequestBody User user){
        try {
            if (baseUtils.getIdentity() == 1){
                userService.save(user);
                return Result.success("添加成功");
            }else{
                return Result.fail("无权限");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.fail("失败");
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public Result<Object> deleteUser(@PathVariable Integer id){
        try {
            if (baseUtils.getIdentity() == 1){
                userService.removeById(id);
                return Result.success("删除成功");
            }else{
                return Result.success("无权限");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.fail("失败");
    }

}
