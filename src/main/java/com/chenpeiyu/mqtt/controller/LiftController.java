package com.chenpeiyu.mqtt.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenpeiyu.mqtt.domain.Lift;
import com.chenpeiyu.mqtt.domain.User;
import com.chenpeiyu.mqtt.service.LiftService;
import com.chenpeiyu.mqtt.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lift")
public class LiftController {
    @Autowired
    LiftService liftService;

    // 查询单个
    @GetMapping("/{id}")
    public Result<Object> getLiftById(@PathVariable Integer id){
        try {
            return Result.success(liftService.getById(id));
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.fail("获取详情失败");
    }

    // 分页查询
    @GetMapping("/list")
    public Result<Object> getLift(@RequestParam("size") Integer size, @RequestParam("current") Integer current){
        try {
            LambdaQueryWrapper<Lift> queryWrapper = Wrappers.lambdaQuery();
            Page<Lift> page = new Page<>(current, size);
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