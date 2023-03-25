package com.chenpeiyu.mqtt.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    Integer id;
    String account;
    String password;
    String unitName;
    Integer ifAdministrators;
    Integer phoneNumber;
    String description;
}
