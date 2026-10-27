package com.mall.common.utils;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET =
            "mall-cloud-secret-key-must-be-long-enough";

    private static final SecretKey KEY =
            Keys.hmacShaKeyFor(
                    SECRET.getBytes()
            );

    /**
     * 生成token
     */
    public static String createToken(Long userId){
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(new Date())
                // 过期时间 7天
                .expiration(
                        new Date(
                            System.currentTimeMillis()
                            + 700L * 24 * 60 * 60 * 1000
                        )
                )
                .signWith(KEY)
                .compact();
    }

    /**
     * 解析token获取用户id
     */
    public static Long getUserId(String token){
        Claims claims =
                Jwts.parser()
                        .verifyWith(KEY)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
        return Long.valueOf(
                claims.getSubject()
        );
    }

}