package com.chenpeiyu.mqtt;

import com.chenpeiyu.mqtt.dao.LiftMapper;
import com.chenpeiyu.mqtt.dao.LiftTypeMapper;
import com.chenpeiyu.mqtt.domain.Lift;
import com.chenpeiyu.mqtt.domain.LiftType;
import com.chenpeiyu.mqtt.domain.User;
import com.chenpeiyu.mqtt.domainiDto.LiftDto;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MqttSpringbootDemoApplicationTests {

    @Autowired
    LiftMapper liftMapper;

    @Test
    void contextLoads() {
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

}
