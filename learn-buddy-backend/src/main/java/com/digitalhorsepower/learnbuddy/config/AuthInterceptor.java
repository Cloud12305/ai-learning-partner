package com.digitalhorsepower.learnbuddy.config;

import com.digitalhorsepower.learnbuddy.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("拦截器处理路径: {}", requestURI);

        // 放行登录、注册和健康检查等公开接口
        if (requestURI.startsWith("/api/users/login") ||
                requestURI.startsWith("/api/users/register") ||
                requestURI.startsWith("/api/users/health")) {
            log.info("放行公开接口: {}", requestURI);
            return true;
        }

        // 检查 Authorization 头
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("未提供有效的 Authorization 头");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\": false, \"message\": \"未提供认证令牌\"}");
            return false;
        }

        String token = authHeader.substring(7);
        log.info("提取的 token 长度: {}", token.length());
        log.info("Token 前20字符: {}", token.substring(0, Math.min(20, token.length())));

        // 验证 token
        if (!jwtUtil.validateToken(token)) {
            log.warn("Token 验证失败");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\": false, \"message\": \"认证令牌无效或已过期\"}");
            return false;
        }

        // 提取用户信息并设置到请求属性中
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            String username = jwtUtil.getUsernameFromToken(token);
            String role = jwtUtil.getRoleFromToken(token);

            if (userId == null) {
                log.warn("无法从 Token 中提取用户ID");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"success\": false, \"message\": \"认证信息不完整\"}");
                return false;
            }

            request.setAttribute("userId", userId);
            request.setAttribute("username", username);
            request.setAttribute("role", role);

            log.info("认证成功 - 用户ID: {}, 用户名: {}, 角色: {}", userId, username, role);
            return true;

        } catch (Exception e) {
            log.error("处理认证信息时发生错误: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"服务器内部错误\"}");
            return false;
        }
    }
}