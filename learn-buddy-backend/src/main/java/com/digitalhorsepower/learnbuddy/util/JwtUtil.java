package com.digitalhorsepower.learnbuddy.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret:learnbuddy-secret-key-2024-ai-learning-partner-spring-boot}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // 24小时
    private long expiration;

    private SecretKey getSigningKey() {
        // 确保密钥长度足够，使用安全的密钥生成方式
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Long userId, String username, String role) {
        log.info("生成 token 用户: {}, 角色: {}", username, role);

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("role", role);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        log.info("生成的 token 详情:");
        log.info("  - 长度: {}", token.length());
        log.info("  - 包含点: {}", token.chars().filter(ch -> ch == '.').count() == 2);
        log.info("  - 部分数量: {}", token.split("\\.").length);
        log.info("  - 前20字符: {}", token.substring(0, Math.min(20, token.length())));

        return token;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            log.info("✅ Token 验证成功");
            return true;
        } catch (ExpiredJwtException e) {
            log.error("❌ Token 已过期: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("❌ 不支持的 Token: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("❌ Token 格式错误: {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("❌ Token 签名验证失败: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("❌ Token 参数错误: {}", e.getMessage());
        } catch (Exception e) {
            log.error("❌ Token 验证异常: {}", e.getMessage());
        }
        return false;
    }

    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String subject = claims.getSubject();
            Long userId = Long.parseLong(subject);
            log.info("✅ 从 Token 解析用户ID: {}", userId);
            return userId;
        } catch (Exception e) {
            log.warn("❌ 解析用户ID失败: {}", e.getMessage());
            // 返回默认值或抛出异常，根据你的业务需求
            return null;
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.get("username", String.class);
            log.info("✅ 从 Token 解析用户名: {}", username);
            return username;
        } catch (Exception e) {
            log.warn("❌ 解析用户名失败: {}", e.getMessage());
            return null;
        }
    }

    public String getRoleFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String role = claims.get("role", String.class);
            log.info("✅ 从 Token 解析角色: {}", role);
            return role;
        } catch (Exception e) {
            log.warn("❌ 解析角色失败: {}", e.getMessage());
            return "STUDENT"; // 默认角色
        }
    }
}