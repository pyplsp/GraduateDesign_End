package com.chenpeiyu.mqtt.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chenpeiyu.mqtt.dao.UserMapper;
import com.chenpeiyu.mqtt.domain.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    public static final long EXPIRATION_TIME = 3600000*5; // 1 小时
    public static final String SECRET_KEY = "duLk1351Moon58oi5ee9Rr5gt2eR151nf5oNe14522wor1Ngn858SD6KJbi8os";

    public static String createToken(Integer id,String account, String password) {

        Map<String, Object> claims = new HashMap<>();
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        claims.put("id", id);
        claims.put("username", account);
        claims.put("password", password);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            // Invalid JWT signature
        } catch (MalformedJwtException e) {
            // Invalid JWT token
        } catch (ExpiredJwtException e) {
            // Expired JWT token
        } catch (UnsupportedJwtException e) {
            // Unsupported JWT token
        } catch (IllegalArgumentException e) {
            // JWT claims string is empty
        }
        return false;
    }
}
