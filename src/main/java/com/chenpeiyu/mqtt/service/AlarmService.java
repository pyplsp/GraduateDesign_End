package com.chenpeiyu.mqtt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenpeiyu.mqtt.domain.Alarm;
import com.chenpeiyu.mqtt.domainiDto.AlarmDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AlarmService extends IService<Alarm> {
    IPage<AlarmDto> pySelectPage(Integer _identity,
                                 String liftCode, String alarmTypeName, Integer alarmStatus,
                                 Integer size, Integer current);
    AlarmDto pySelectOne(Integer _identity,Integer alarmId);
    void pyUnlockAlarm(Integer _identity, Integer alarmId);

    Long pySelectAllAlarm(Integer _identity);

    Long pySelectAllAlarmRemove(Integer _identity);

    List<Map<String, Object>> pySelectLAlarmTypePie(Integer _identity);

    List<Map<String, Object>> pySelectLAlarmStatusPie(Integer _identity);

    List<HashMap<String,Object>> pySelectAlarmTender(Integer _identity);

}
