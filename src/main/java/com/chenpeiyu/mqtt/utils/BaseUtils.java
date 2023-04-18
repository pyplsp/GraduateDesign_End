package com.chenpeiyu.mqtt.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class BaseUtils {
    @Autowired
    private HttpServletRequest request;

    // 通过请求头获取 id 值，用于判断当前用户的id
    public Integer getIdentity(){
        return (Integer) request.getAttribute("id");
    }

    public String nowTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime = new Date();
        return formatter.format(currentTime);
    }
}
