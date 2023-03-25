package com.chenpeiyu.mqtt.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("alarm")
public class Alarm {
    @TableId(type = IdType.AUTO)
    Integer id;
    Integer liftId;
    Integer alarmType;
    Date alarmTime;
    Date alarmRemoveTime;
    Integer alarmStatus;
    Integer hasPerson;
    Integer personNum;
    Integer currFloor;
    Integer ifFlat;
    String description;
}
