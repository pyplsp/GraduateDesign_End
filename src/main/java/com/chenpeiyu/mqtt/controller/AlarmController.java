package com.chenpeiyu.mqtt.controller;

import com.alibaba.fastjson.JSONObject;
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

}
