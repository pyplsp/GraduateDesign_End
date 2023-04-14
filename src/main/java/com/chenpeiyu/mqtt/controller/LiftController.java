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
            return Result.success(liftService.pySelectOne(baseUtils.getIdentity(),id));
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
        Integer userId = jsonObject.getIntValue("userId"); // 若无返回0
        Integer liftTypeId = jsonObject.getIntValue("liftTypeId"); // 若无返回0
        String liftCode = jsonObject.getString("liftCode"); //若无返回null
        String liftName = jsonObject.getString("liftName"); //若无返回null
        try {
            return Result.success(
                    liftService.pySelectPage(baseUtils.getIdentity()
                    ,userId,liftTypeId,liftCode,liftName,
                    size,current));
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
