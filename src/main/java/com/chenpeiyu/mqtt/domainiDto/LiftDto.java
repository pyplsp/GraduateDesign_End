package com.chenpeiyu.mqtt.domainiDto;

import lombok.Data;

@Data
public class LiftDto {

    Integer id;
    Integer userId;
    String liftCode;
    String liftName;
    String liftTypeId;
    double positionX;
    double positionY;
    Integer internetStatus;
    String description;

    String liftTypeName;

    String unitName;
}
