package com.chenpeiyu.mqtt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenpeiyu.mqtt.dao.AlarmMapper;
import com.chenpeiyu.mqtt.domain.Alarm;
import com.chenpeiyu.mqtt.service.AlarmService;
import org.springframework.stereotype.Service;

@Service
public class AlarmServiceImpl extends ServiceImpl<AlarmMapper, Alarm> implements AlarmService {

}
