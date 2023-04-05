package com.chenpeiyu.mqtt.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("liftType")
public class LiftType {
    @TableId(type = IdType.AUTO)
    Integer id;
    String liftTypeName;
}
