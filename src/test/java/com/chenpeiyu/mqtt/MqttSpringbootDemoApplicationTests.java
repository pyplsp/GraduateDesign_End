package com.chenpeiyu.mqtt;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenpeiyu.mqtt.dao.AlarmMapper;
import com.chenpeiyu.mqtt.dao.LiftMapper;
import com.chenpeiyu.mqtt.domain.Alarm;
import com.chenpeiyu.mqtt.domain.Lift;
import com.chenpeiyu.mqtt.domain.LiftType;
import com.chenpeiyu.mqtt.domain.User;
import com.chenpeiyu.mqtt.domainiDto.LiftDto;
import com.chenpeiyu.mqtt.domainiDto.AlarmDto;
import com.chenpeiyu.mqtt.utils.BaseUtils;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@SpringBootTest
class MqttSpringbootDemoApplicationTests {

    @Autowired
    LiftMapper liftMapper;

    @Autowired
    BaseUtils baseUtils;

    @Autowired
    AlarmMapper alarmMapper;

    @Test
    void test1() {
        // 联表案例
        MPJLambdaWrapper<Lift> queryWrapper = new MPJLambdaWrapper<Lift>().selectAll(Lift.class)
                .select(LiftType::getLiftTypeName)
                .select(User::getUnitName)
                .leftJoin(LiftType.class,LiftType::getId,Lift::getLiftTypeId)
                .leftJoin(User.class,User::getId,Lift::getUserId)
                .eq(Lift::getId,2);
        LiftDto liftDto = liftMapper.selectJoinOne(LiftDto.class,queryWrapper);
        System.out.println(liftDto);
    }

    @Test
    void test2(){
        // System.out.println(baseUtils.nowTime());
        System.out.println(LocalDate.now().toString());


        System.out.println();
    }

    @Test
    void test3(){
        MPJLambdaWrapper<Alarm> queryWrapper = new MPJLambdaWrapper<Alarm>().selectAll(Alarm.class)
                .select(Lift::getLiftCode)
                .leftJoin(Lift.class,Lift::getId,Alarm::getLiftId)
                .leftJoin(User.class,User::getId,Lift::getUserId)
                .eq(User::getId,1);

        IPage<AlarmDto> page = alarmMapper.selectJoinPage(new Page<>(1,10), AlarmDto.class,queryWrapper);
        System.out.println(page.getRecords());
    }


}
