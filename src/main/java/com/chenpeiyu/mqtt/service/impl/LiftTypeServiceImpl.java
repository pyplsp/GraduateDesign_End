package com.chenpeiyu.mqtt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenpeiyu.mqtt.dao.LiftTypeMapper;
import com.chenpeiyu.mqtt.domain.LiftType;
import com.chenpeiyu.mqtt.service.LiftTypeService;
import org.springframework.stereotype.Service;

@Service
public class LiftTypeServiceImpl extends ServiceImpl<LiftTypeMapper, LiftType> implements LiftTypeService {

}
