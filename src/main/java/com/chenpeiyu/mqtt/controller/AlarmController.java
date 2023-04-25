package com.chenpeiyu.mqtt.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.chenpeiyu.mqtt.domain.Alarm;
import com.chenpeiyu.mqtt.service.AlarmService;
import com.chenpeiyu.mqtt.utils.BaseUtils;
import com.chenpeiyu.mqtt.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alarm")
public class AlarmController {
    @Autowired
    private BaseUtils baseUtils;

    @Autowired
    private AlarmService alarmService;

    // 分页查询（可带模糊查询）
    @PostMapping("/list")
    public Result<Object> searchAlarm(
            @RequestBody String json,
            @RequestParam("size") Integer size,
            @RequestParam("current") Integer current){
        // 模糊查询的条件
        JSONObject jsonObject = JSONObject.parseObject(json);
        String liftCode = jsonObject.getString("liftCode"); //若无返回null
        String alarmTypeName = jsonObject.getString("alarmTypeName"); //若无返回null
        Integer alarmStatus = jsonObject.getInteger("alarmStatus"); // 若无返回null (这个和getIntValue不一样)
        try {
            return Result.success(alarmService.pySelectPage(baseUtils.getIdentity(),liftCode,alarmTypeName,alarmStatus,size,current));
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @GetMapping("/{alarmId}")
    public Result<Object> getAlarmById(@PathVariable Integer alarmId){
        try {
            return Result.success(alarmService.pySelectOne(baseUtils.getIdentity(),alarmId));
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.fail("获取详情失败");
    }

    @GetMapping("/unlock/{alarmId}")
    public Result<Object> unlockAlarm(@PathVariable Integer alarmId){
        try {
            LambdaUpdateWrapper<Alarm> queryWrapper = new LambdaUpdateWrapper<Alarm>()
                    .eq(Alarm::getId,alarmId)
                    .set(Alarm::getAlarmStatus,-1);
            alarmService.update(queryWrapper);
            return Result.success("成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.fail("失败");
    }


}
