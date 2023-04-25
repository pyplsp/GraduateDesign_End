package com.chenpeiyu.mqtt.service.impl;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenpeiyu.mqtt.dao.AlarmMapper;
import com.chenpeiyu.mqtt.domain.Alarm;
import com.chenpeiyu.mqtt.domain.Lift;
import com.chenpeiyu.mqtt.domain.User;
import com.chenpeiyu.mqtt.domainiDto.AlarmDto;
import com.chenpeiyu.mqtt.service.AlarmService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlarmServiceImpl extends ServiceImpl<AlarmMapper, Alarm> implements AlarmService {

    @Autowired
    AlarmMapper alarmMapper;

    @Override
    public IPage<AlarmDto> pySelectPage(Integer _identity,
                                     String liftCode, String alarmTypeName, Integer alarmStatus,
                                     Integer size, Integer current) {
        MPJLambdaWrapper<Alarm> queryWrapper = new MPJLambdaWrapper<Alarm>().selectAll(Alarm.class)
                .select(Lift::getLiftCode)
                .select(User::getId)
                .leftJoin(Lift.class,Lift::getId,Alarm::getLiftId)
                .leftJoin(User.class,User::getId,Lift::getUserId);
        queryWrapper.eq(User::getId,_identity);
        if(alarmStatus != null)
            queryWrapper.eq(Alarm::getAlarmStatus,alarmStatus); // 状态
        queryWrapper.like(Lift::getLiftCode,liftCode)
                    .like(Alarm::getAlarmTypeName,alarmTypeName)
                    .orderByDesc(Alarm::getAlarmTime); // 电梯设备代码，告警类型名称
        return alarmMapper.selectJoinPage(new Page<>(current,size), AlarmDto.class,queryWrapper);
    }

    @Override
    public AlarmDto pySelectOne(Integer _identity, Integer alarmId) {
        MPJLambdaWrapper<Alarm> queryWrapper = makeQueryWrapper(_identity,alarmId);
        return alarmMapper.selectJoinOne(AlarmDto.class,queryWrapper);
    }

    @Override
    public void pyUnlockAlarm(Integer _identity, Integer alarmId) {
        MPJLambdaWrapper<Alarm> queryWrapper = makeQueryWrapper(_identity,alarmId);
        queryWrapper.eq(Alarm::getAlarmStatus,1);
        Integer i = alarmMapper.selectJoinOne(AlarmDto.class,queryWrapper).getId();
        LambdaUpdateWrapper<Alarm> updateWrapper = new LambdaUpdateWrapper<Alarm>()
                .eq(Alarm::getId,i)
                .set(Alarm::getAlarmStatus,-1);
        alarmMapper.update(null,updateWrapper);
    }

    private MPJLambdaWrapper<Alarm> makeQueryWrapper(Integer _identity, Integer alarmId){
        return  new MPJLambdaWrapper<Alarm>().selectAll(Alarm.class)
                    .select(Lift::getLiftCode)
                    .select("t2.id as userId") // 默认情况下 t alarm,t1 lift,t2 user
                    .leftJoin(Lift.class,Lift::getId,Alarm::getLiftId)
                    .leftJoin(User.class,User::getId,Lift::getUserId)
                    .eq(User::getId,_identity)
                    .eq(Alarm::getId,alarmId);
    }

}
