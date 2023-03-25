package com.chenpeiyu.mqtt.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("rescue")
public class Rescue {
    @TableId(type = IdType.AUTO)
    Integer id;
    Integer alarmId;
    String rescueUnit;
    Integer rescuerNumber;
    Integer phoneNumber;
    Date rescueTime;
    Integer rescueStatus;
    String description;
}
