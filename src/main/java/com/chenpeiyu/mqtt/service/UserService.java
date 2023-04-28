package com.chenpeiyu.mqtt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenpeiyu.mqtt.domain.User;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;
import java.util.Map;


public interface UserService extends IService<User> {
    List<Map<String,Object>> pySelectUnitName(Integer _identity);
}
