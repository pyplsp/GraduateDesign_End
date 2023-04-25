package com.chenpeiyu.mqtt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenpeiyu.mqtt.domain.Alarm;
import com.chenpeiyu.mqtt.domainiDto.AlarmDto;

public interface AlarmService extends IService<Alarm> {
    IPage<AlarmDto> pySelectPage(Integer _identity,
                                 String liftCode, String alarmTypeName, Integer alarmStatus,
                                 Integer size, Integer current);
    AlarmDto pySelectOne(Integer _identity,Integer alarmId);
    void pyUnlockAlarm(Integer _identity, Integer alarmId);

}
