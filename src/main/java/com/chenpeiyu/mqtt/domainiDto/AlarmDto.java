package com.chenpeiyu.mqtt.domainiDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

@Data
public class AlarmDto {
    Integer id;
    Integer liftId;
    String alarmTypeName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Timestamp alarmTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Timestamp alarmRemoveTime;
    Integer alarmStatus;
    Integer personNum;
    Integer currFloor;
    Integer ifFlat;
    String description;
    String alarmTypeCode;

    String liftCode;

    Integer userId;
    String unitName;


}
