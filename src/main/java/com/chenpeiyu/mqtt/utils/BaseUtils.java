package com.chenpeiyu.mqtt.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class BaseUtils {
    @Autowired
    private HttpServletRequest request;

    // 通过请求头获取 id 值，用于判断当前用户的id
    public Integer getIdentity(){
        return (Integer) request.getAttribute("id");
    }
}
