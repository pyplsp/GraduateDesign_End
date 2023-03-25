package com.chenpeiyu.mqtt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chenpeiyu.mqtt.domain.User;
import com.chenpeiyu.mqtt.service.UserService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
