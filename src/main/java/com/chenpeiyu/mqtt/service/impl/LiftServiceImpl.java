package com.chenpeiyu.mqtt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenpeiyu.mqtt.dao.LiftMapper;
import com.chenpeiyu.mqtt.domain.Lift;
import com.chenpeiyu.mqtt.service.LiftService;
import org.springframework.stereotype.Service;

@Service
public class LiftServiceImpl extends ServiceImpl<LiftMapper, Lift> implements LiftService {

}
