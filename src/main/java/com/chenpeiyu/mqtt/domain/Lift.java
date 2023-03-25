package com.chenpeiyu.mqtt.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("lift")
public class Lift {
    @TableId(type = IdType.AUTO)
    Integer id;
    Integer userId;
    String liftCode;
    String liftName;
    String liftType;
    double positionX;
    double positionY;
    Integer internetStatus;
    String description;
}
