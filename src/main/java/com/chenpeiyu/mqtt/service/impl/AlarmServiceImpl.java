package com.chenpeiyu.mqtt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenpeiyu.mqtt.dao.AlarmMapper;
import com.chenpeiyu.mqtt.domain.Alarm;
import com.chenpeiyu.mqtt.domain.Lift;
import com.chenpeiyu.mqtt.domain.User;
import com.chenpeiyu.mqtt.domainiDto.AlarmDto;
import com.chenpeiyu.mqtt.service.AlarmService;
import com.chenpeiyu.mqtt.utils.BaseUtils;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.*;

@Service
public class AlarmServiceImpl extends ServiceImpl<AlarmMapper, Alarm> implements AlarmService {

    @Autowired
    AlarmMapper alarmMapper;

    @Autowired
    BaseUtils baseUtils;

    @Override
    public IPage<AlarmDto> pySelectPage(Integer _identity,
                                     String liftCode, String alarmTypeName, Integer alarmStatus,
                                     Integer size, Integer current) {
        MPJLambdaWrapper<Alarm> queryWrapper = makeQueryWrapper();
        if(_identity != 1)
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
        MPJLambdaWrapper<Alarm> queryWrapper = makeQueryWrapper()
                .eq(Alarm::getId,alarmId);
        if(_identity != 1)
            queryWrapper.eq(User::getId,_identity);
        return alarmMapper.selectJoinOne(AlarmDto.class,queryWrapper);
    }

    @Override
    public void pyUnlockAlarm(Integer _identity, Integer alarmId) {
        MPJLambdaWrapper<Alarm> queryWrapper = makeQueryWrapper();
        queryWrapper.eq(Alarm::getAlarmStatus,1)
                .eq(User::getId,_identity)
                .eq(Alarm::getId,alarmId);
        Integer i = alarmMapper.selectJoinOne(AlarmDto.class,queryWrapper).getId();
        LambdaUpdateWrapper<Alarm> updateWrapper = new LambdaUpdateWrapper<Alarm>()
                .eq(Alarm::getId,i)
                .set(Alarm::getAlarmStatus,-1)
                .set(Alarm::getAlarmRemoveTime, Timestamp.valueOf(baseUtils.nowTime()));
        alarmMapper.update(null,updateWrapper);
    }
    @Override
    public Long pySelectAllAlarm(Integer _identity){
        MPJLambdaWrapper<Alarm> mpjLambdaWrapper = makeQueryWrapperOnlyJoin();
        if(_identity != 1)
            mpjLambdaWrapper.eq(User::getId,_identity);
        return alarmMapper.selectJoinCount(mpjLambdaWrapper);
    }

    @Override
    public Long pySelectAllAlarmRemove(Integer _identity) {
        MPJLambdaWrapper<Alarm> mpjLambdaWrapper = makeQueryWrapperOnlyJoin();
        mpjLambdaWrapper.eq(User::getId,_identity)
                .between(Alarm::getAlarmStatus,-1,0);
        if(_identity != 1)
            mpjLambdaWrapper.eq(User::getId,_identity);
        return alarmMapper.selectJoinCount(mpjLambdaWrapper);
    }

    @Override
    public List<Map<String, Object>> pySelectLAlarmTypePie(Integer _identity) {
        MPJLambdaWrapper<Alarm> lambdaQueryWrapper = makeQueryWrapperOnlyJoin();
        lambdaQueryWrapper.select("count(*) as count")
                .select(Alarm::getAlarmTypeName)
                .groupBy(Alarm::getAlarmTypeName);
        if (_identity !=1)
            lambdaQueryWrapper.eq(User::getId,_identity);
        return alarmMapper.selectMaps(lambdaQueryWrapper);
    }

    @Override
    public List<Map<String, Object>> pySelectLAlarmStatusPie(Integer _identity) {
        MPJLambdaWrapper<Alarm> lambdaQueryWrapper = makeQueryWrapperOnlyJoin();
        lambdaQueryWrapper.select("count(*) as count")
                .select(Alarm::getAlarmStatus)
                .groupBy(Alarm::getAlarmStatus);
        if (_identity !=1)
            lambdaQueryWrapper.eq(User::getId,_identity);
        return alarmMapper.selectMaps(lambdaQueryWrapper);
    }

    @Override
    public List<HashMap<String,Object>> pySelectAlarmTender(Integer _identity) {
        List<HashMap<String,Object>> list = new ArrayList<>();
        for (int i = 6; i>=0; i--){
            HashMap<String,Object> map= new HashMap<>();
            String[] s = baseUtils.getLastXDays(i);
            MPJLambdaWrapper<Alarm> mpjLambdaWrapper = makeQueryWrapperOnlyJoin()
                    .between(Alarm::getAlarmTime,s[0],s[1]);
            if (_identity !=1)
                mpjLambdaWrapper.eq(User::getId,_identity);
            map.put("date",s[0].substring(0,10));
            map.put("count",alarmMapper.selectCount(mpjLambdaWrapper));
            list.add(map);
        }
        return list;
    }

    @Override
    public List<HashMap<String, Object>> pySelectAlarmRemoveTender(Integer _identity) {
        List<HashMap<String,Object>> list = new ArrayList<>();
        for (int i = 6; i>=0; i--){
            HashMap<String,Object> map= new HashMap<>();
            String[] s = baseUtils.getLastXDays(i);
            MPJLambdaWrapper<Alarm> mpjLambdaWrapper = makeQueryWrapperOnlyJoin()
                    .between(Alarm::getAlarmRemoveTime,s[0],s[1]);
            if (_identity !=1)
                mpjLambdaWrapper.eq(User::getId,_identity);
            map.put("date",s[0].substring(0,10));
            map.put("count",alarmMapper.selectCount(mpjLambdaWrapper));
            list.add(map);
        }
        return list;
    }


    private MPJLambdaWrapper<Alarm> makeQueryWrapperOnlyJoin(){
        return new MPJLambdaWrapper<Alarm>()
                .leftJoin(Lift.class,Lift::getId,Alarm::getLiftId)
                .leftJoin(User.class,User::getId,Lift::getUserId);
    }

    private MPJLambdaWrapper<Alarm> makeQueryWrapper(){
        return new MPJLambdaWrapper<Alarm>().selectAll(Alarm.class)
                    .select(Lift::getLiftCode)
                    .select("t2.id as userId") // 默认情况下 t alarm,t1 lift,t2 user
                    .select(User::getUnitName)
                    .leftJoin(Lift.class,Lift::getId,Alarm::getLiftId)
                    .leftJoin(User.class,User::getId,Lift::getUserId);
    }
}
