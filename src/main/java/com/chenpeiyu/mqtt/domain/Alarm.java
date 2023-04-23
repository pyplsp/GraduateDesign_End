package com.chenpeiyu.mqtt.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("alarm")
public class Alarm {
    @TableId(type = IdType.AUTO)
    String id;
    Integer liftId;
    String alarmTypeName;
    String alarmTime;
    String alarmRemoveTime;
    Integer alarmStatus;
    Integer personNum;
    Integer currFloor;
    Integer ifFlat;
    String description;
}
