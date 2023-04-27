package com.chenpeiyu.mqtt.controller;


import com.chenpeiyu.mqtt.domain.Lift;
import com.chenpeiyu.mqtt.service.AlarmService;
import com.chenpeiyu.mqtt.service.LiftService;
import com.chenpeiyu.mqtt.service.LiftTypeService;
import com.chenpeiyu.mqtt.service.UserService;
import com.chenpeiyu.mqtt.utils.BaseUtils;
import com.chenpeiyu.mqtt.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/overview")
public class OverviewController {
    @Autowired
    LiftService liftService;
    @Autowired
    LiftTypeService liftTypeService;
    @Autowired
    AlarmService alarmService;
    @Autowired
    UserService userService;
    @Autowired
    BaseUtils baseUtils;

    @GetMapping
    public Result<Object> baseData(){
        Integer _identity = baseUtils.getIdentity();
        Map<String,Object> overviewMap = new HashMap<>();
        // 基础数据
        overviewMap.put("liftNum",liftService.pySelectAllLifts(baseUtils.getIdentity()));
        overviewMap.put("internetNum",liftService.pySelectAllInternet(baseUtils.getIdentity()));
        overviewMap.put("alarmNum",alarmService.pySelectAllAlarm(_identity));
        overviewMap.put("alarmRemoveNum",alarmService.pySelectAllAlarmRemove(_identity));
        // 饼图
        overviewMap.put("liftTypePIe",liftService.pySelectLIftTypePie(_identity));
        overviewMap.put("alarmTypePie",alarmService.pySelectLAlarmTypePie(_identity));
        overviewMap.put("alarmStatusPie",alarmService.pySelectLAlarmStatusPie(_identity));
        // 趋势图
        overviewMap.put("alarmTender",alarmService.pySelectAlarmTender(_identity));


        return Result.success(overviewMap);
    }
}
