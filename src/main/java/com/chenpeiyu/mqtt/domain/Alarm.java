package com.chenpeiyu.mqtt.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

@Data
@TableName("alarm")
public class Alarm {
    @TableId(type = IdType.AUTO)
    Integer id;
    Integer liftId;
    String alarmTypeName;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Timestamp alarmTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Timestamp alarmRemoveTime;
    Integer alarmStatus;
    Integer personNum;
    Integer currFloor;
    Integer ifFlat;
    String description;
    String alarmTypeCode;
}
