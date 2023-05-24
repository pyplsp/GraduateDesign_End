package com.chenpeiyu.mqtt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenpeiyu.mqtt.domain.Lift;
import com.chenpeiyu.mqtt.domain.User;

import java.util.List;
import java.util.Map;


public interface UserService extends IService<User> {
    List<Map<String,Object>> pySelectUnitName(Integer _identity);

    IPage<User> pySelectUsers(Integer size, Integer current);
}
