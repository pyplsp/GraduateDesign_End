package com.chenpeiyu.mqtt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chenpeiyu.mqtt.domain.Lift;
import com.chenpeiyu.mqtt.domainiDto.LiftDto;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LiftMapper extends MPJBaseMapper<Lift> {

}
