package com.chenpeiyu.mqtt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chenpeiyu.mqtt.domain.Alarm;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlarmMapper extends BaseMapper<Alarm> {
}
