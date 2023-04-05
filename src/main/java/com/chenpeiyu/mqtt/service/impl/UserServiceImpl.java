package com.chenpeiyu.mqtt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenpeiyu.mqtt.dao.UserMapper;
import com.chenpeiyu.mqtt.domain.User;
import com.chenpeiyu.mqtt.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
