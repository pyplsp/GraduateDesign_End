package com.chenpeiyu.mqtt.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenpeiyu.mqtt.dao.LiftMapper;
import com.chenpeiyu.mqtt.domain.Lift;
import com.chenpeiyu.mqtt.domain.LiftType;
import com.chenpeiyu.mqtt.domain.User;
import com.chenpeiyu.mqtt.domainiDto.LiftDto;
import com.chenpeiyu.mqtt.service.LiftService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LiftServiceImpl extends ServiceImpl<LiftMapper, Lift> implements LiftService {

    @Autowired
    LiftMapper liftMapper;

    @Override
    public LiftDto pySelectOne(Integer _identity,Integer liftId){
        MPJLambdaWrapper<Lift> queryWrapper = new MPJLambdaWrapper<Lift>().selectAll(Lift.class)
                .select(LiftType::getLiftTypeName)
                .select(User::getUnitName)
                .leftJoin(LiftType.class,LiftType::getId,Lift::getLiftTypeId)
                .leftJoin(User.class,User::getId,Lift::getUserId)
                .eq(Lift::getId,liftId);
        if(_identity != 1)
            queryWrapper.eq(Lift::getUserId,_identity); // 非主账号需要验证 该电梯是否属于该账户
        return liftMapper.selectJoinOne(LiftDto.class,queryWrapper);
    }

    @Override
    public IPage<LiftDto> pySelectPage(Integer _identity,
                                       Integer userId, Integer liftTypeId, String liftCode, String liftName,
                                       Integer size, Integer current) {
        MPJLambdaWrapper<Lift> queryWrapper = new MPJLambdaWrapper<Lift>().selectAll(Lift.class)
                .select(LiftType::getLiftTypeName)
                .select(User::getUnitName)
                .leftJoin(LiftType.class,LiftType::getId,Lift::getLiftTypeId)
                .leftJoin(User.class,User::getId,Lift::getUserId);

        // 非主账号
        if(_identity != 1)
            queryWrapper.eq(Lift::getUserId,_identity);
        // 主账号
        else
            if (userId != 0)
                // userId不为0说明前端有传确切的userId
                queryWrapper.like(Lift::getUserId,userId);

        // liftTypeId不为0说明前端有传确切的liftTypeId
        if(liftTypeId != 0){
            queryWrapper.like(Lift::getLiftTypeId,liftTypeId);
        }
        // 模糊查询 设备代码以及设备名称
        queryWrapper
                .like(Lift::getLiftCode,liftCode)
                .like(Lift::getLiftName,liftName);

        return liftMapper.selectJoinPage(new Page<>(current,size), LiftDto.class,queryWrapper);
    }
}
