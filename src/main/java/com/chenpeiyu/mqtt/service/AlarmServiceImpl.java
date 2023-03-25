package com.chenpeiyu.mqtt.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenpeiyu.mqtt.dao.AlarmMapper;
import com.chenpeiyu.mqtt.domain.Alarm;
import org.springframework.stereotype.Service;

@Service
public class AlarmServiceImpl extends ServiceImpl<AlarmMapper, Alarm> implements AlarmService{

}
