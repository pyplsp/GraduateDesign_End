package com.chenpeiyu.mqtt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chenpeiyu.mqtt.domain.Test;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper extends BaseMapper<Test> {

}
