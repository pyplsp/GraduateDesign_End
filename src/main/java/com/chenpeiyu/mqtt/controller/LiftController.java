package com.chenpeiyu.mqtt.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenpeiyu.mqtt.domain.Lift;
import com.chenpeiyu.mqtt.service.LiftService;
import com.chenpeiyu.mqtt.utils.BaseUtils;
import com.chenpeiyu.mqtt.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lift")
public class LiftController {

    @Autowired
    private BaseUtils baseUtils;
    @Autowired
    private LiftService liftService;

    // 根据id查询单个
    @GetMapping("/{id}")
    public Result<Object> getLiftById(@PathVariable Integer id){
        try {
            if( baseUtils.getIdentity()!= 1){
                // 非主账号
                LambdaQueryWrapper<Lift> queryWrapper = Wrappers.lambdaQuery();
                queryWrapper.eq(Lift::getId,id).eq(Lift::getUserId,baseUtils.getIdentity());
                return Result.success(liftService.getOne(queryWrapper));
            }else {
                // 主账号
                return Result.success(liftService.getById(id));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.fail("获取详情失败");
    }

    // 分页查询（可带模糊查询）
    @PostMapping("/list")
    public Result<Object> searchLift(@RequestBody String json,
                                     @RequestParam("size") Integer size,
                                     @RequestParam("current") Integer current){
        // 模糊查询的条件
        JSONObject jsonObject = JSONObject.parseObject(json);
        Integer userId = jsonObject.getIntValue("userId");
        Integer liftTypeId = jsonObject.getIntValue("liftTypeId");
        String liftCode = jsonObject.getString("liftCode");
        String liftName = jsonObject.getString("liftName");
        try {
            LambdaQueryWrapper<Lift> queryWrapper = Wrappers.lambdaQuery();
            if (userId != 0)
                // userId不为0说明前端有传确切的userId
                queryWrapper.like(Lift::getUserId,userId);
            if(liftTypeId != 0)
                // liftTypeId不为0说明前端有传确切的liftTypeId
                queryWrapper.like(Lift::getLiftTypeId,liftTypeId);
            queryWrapper.like(Lift::getLiftCode,liftCode)
                    .like(Lift::getLiftName,liftName);
            Page<Lift> page = new Page<>(current, size);
            if( baseUtils.getIdentity()!= 1){
                // 子账号
                queryWrapper.eq(Lift::getUserId,baseUtils.getIdentity());
            }
            return Result.success(liftService.page(page,queryWrapper));
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.fail("未知错误");
    }

    // 添加电梯
    @PostMapping
    public Result<Object> createLift(@RequestBody Lift lift){
        try{
            liftService.save(lift);
            return Result.success("添加成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.success("添加失败");
    }

    // 删除电梯
    @DeleteMapping("/{id}")
    public Result<Object> deleteLift(@PathVariable Integer id){
        try {
            liftService.removeById(id);
            return Result.success("删除成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.success("失败");
    }

    // 更新电梯
    @PutMapping("/{id}")
    public Result<Object> updateLift(@PathVariable Integer id,@RequestBody Lift lift){
        try {
            lift.setId(id);
            liftService.updateById(lift);
            return Result.success("更新成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.success("更新失败");
    }

}
