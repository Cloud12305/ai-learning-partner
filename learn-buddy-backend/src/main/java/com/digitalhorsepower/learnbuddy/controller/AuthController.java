package com.digitalhorsepower.learnbuddy.controller;

import com.digitalhorsepower.learnbuddy.entity.User;
import com.digitalhorsepower.learnbuddy.service.UserService;
import com.digitalhorsepower.learnbuddy.util.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // 使用正确的方法名（小写开头）
            Optional<User> userOptional = userService.validateLogin(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            );

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

                Map<String, Object> response = new HashMap<>();
                response.put("token", token);

                // 构建用户信息（不包含敏感信息）
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("id", user.getId());
                userInfo.put("username", user.getUsername());
                userInfo.put("role", user.getRole());
                userInfo.put("studentId", user.getStudentId());
                userInfo.put("name", user.getName());
                userInfo.put("email", user.getEmail());
                userInfo.put("avatarUrl", user.getAvatarUrl());

                response.put("user", userInfo);
                response.put("message", "登录成功");
                response.put("success", true);

                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "用户名或密码错误");
                return ResponseEntity.badRequest().body(errorResponse);
            }
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            String token = jwtUtil.generateToken(createdUser.getId(), createdUser.getUsername(), createdUser.getRole());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);

            // 构建用户信息（不包含敏感信息）
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", createdUser.getId());
            userInfo.put("username", createdUser.getUsername());
            userInfo.put("role", createdUser.getRole());
            userInfo.put("studentId", createdUser.getStudentId());
            userInfo.put("name", createdUser.getName());
            userInfo.put("email", createdUser.getEmail());
            userInfo.put("avatarUrl", createdUser.getAvatarUrl());

            response.put("user", userInfo);
            response.put("message", "注册成功");
            response.put("success", true);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestAttribute Long userId) {
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // 构建用户信息（不包含敏感信息）
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("role", user.getRole());
            userInfo.put("studentId", user.getStudentId());
            userInfo.put("name", user.getName());
            userInfo.put("email", user.getEmail());
            userInfo.put("avatarUrl", user.getAvatarUrl());
            userInfo.put("college", user.getCollege());
            userInfo.put("major", user.getMajor());
            userInfo.put("grade", user.getGrade());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("user", userInfo);

            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "用户不存在");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // 使用 Lombok 注解的 LoginRequest 类
    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
}