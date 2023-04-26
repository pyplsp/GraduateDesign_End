package com.chenpeiyu.mqtt.controller;

import com.chenpeiyu.mqtt.service.LiftTypeService;
import com.chenpeiyu.mqtt.utils.BaseUtils;
import com.chenpeiyu.mqtt.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/liftType")
public class LiftTypeController {
    @Autowired
    private BaseUtils baseUtils;

    @Autowired
    private LiftTypeService liftTypeService;

    @GetMapping
    public Result<Object> searchLiftType(){
        try {
            return Result.success(liftTypeService.list());
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.fail("未知错误");
    }

}
