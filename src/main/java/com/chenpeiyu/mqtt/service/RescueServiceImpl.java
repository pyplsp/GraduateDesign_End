package com.chenpeiyu.mqtt.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenpeiyu.mqtt.dao.RescueMapper;
import com.chenpeiyu.mqtt.domain.Rescue;
import org.springframework.stereotype.Service;

@Service
public class RescueServiceImpl extends ServiceImpl<RescueMapper, Rescue> implements RescueService{

}
