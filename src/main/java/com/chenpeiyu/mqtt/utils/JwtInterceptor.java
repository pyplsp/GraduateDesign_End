package com.chenpeiyu.mqtt.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果是登录请求，则通过
        if ("/user/login".equals(request.getRequestURI())) {
            return true;
        }
        // 获取请求头中的Token
        String token = request.getHeader("Authorization");
        try {
            // 解析Token
            Claims claims = Jwts.parser().setSigningKey(JwtUtils.SECRET_KEY).parseClaimsJws(token.replace("Bearer ", "")).getBody();
            // 将解析出来的用户信息放入request中，方便后续业务处理
            request.setAttribute("id", claims.get("id"));
            return true;
        } catch (Exception e) {
            // 解析失败，返回401未授权
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }
}
