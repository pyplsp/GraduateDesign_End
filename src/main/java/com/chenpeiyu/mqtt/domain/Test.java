package com.chenpeiyu.mqtt.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("test")
public class Test {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;

}