package com.chenpeiyu.mqtt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenpeiyu.mqtt.domain.Lift;
import com.chenpeiyu.mqtt.domainiDto.LiftDto;

public interface LiftService extends IService<Lift> {
    LiftDto pySelectOne(Integer _identity,Integer liftId);

   IPage<LiftDto> pySelectPage(Integer _identity,
                               Integer userId, Integer liftTypeId, String liftCode, String liftName,
                               Integer size, Integer current);
}
