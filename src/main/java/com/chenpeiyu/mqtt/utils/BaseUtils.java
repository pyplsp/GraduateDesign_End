package com.chenpeiyu.mqtt.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    // 获取偏移x个月的每个月第一天和每个月最后一天
    public String[] getLastXMonthFirstAndLastDay(int x, String format) {
        LocalDate now = LocalDate.now();
        LocalDate lastXMonth = now.minusMonths(x);
        LocalDate firstDay = lastXMonth.withDayOfMonth(1);
        LocalDate lastDay = lastXMonth.withDayOfMonth(lastXMonth.lengthOfMonth());
        LocalDateTime firstDateTime = firstDay.atStartOfDay();
        LocalDateTime lastDateTime = lastDay.atTime(23, 59, 59);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return new String[] {firstDateTime.format(formatter), lastDateTime.format(formatter)};
    }


    // 获取偏移x天
    public String[] getLastXDays(int x) {
        String[] result = new String[2];
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(x);
        result[0] = LocalDateTime.of(startDate, LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        result[1] = LocalDateTime.of(startDate, LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return result;
    }

}
