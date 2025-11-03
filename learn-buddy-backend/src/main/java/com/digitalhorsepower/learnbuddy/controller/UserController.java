package com.digitalhorsepower.learnbuddy.controller;

import com.digitalhorsepower.learnbuddy.dto.ApiResponse;
import com.digitalhorsepower.learnbuddy.dto.LoginRequest;
import com.digitalhorsepower.learnbuddy.entity.User;
import com.digitalhorsepower.learnbuddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping
    public ApiResponse<List<User>> getAllUsers() {
        logger.info("获取所有用户列表");
        List<User> users = userService.getAllUsers();
        return ApiResponse.success("获取用户列表成功", users);
    }

    @GetMapping("/{id}")
    public ApiResponse<User> getUserById(@PathVariable Long id) {
        logger.info("根据ID获取用户: {}", id);
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> ApiResponse.success("获取用户成功", value))
                .orElse(ApiResponse.error("用户不存在"));
    }

    @GetMapping("/username/{username}")
    public ApiResponse<User> getUserByUsername(@PathVariable String username) {
        logger.info("根据用户名获取用户: {}", username);
        Optional<User> user = userService.getUserByUsername(username);
        return user.map(value -> ApiResponse.success("获取用户成功", value))
                .orElse(ApiResponse.error("用户不存在"));
    }

    @PostMapping
    public ApiResponse<User> createUser(@RequestBody User user) {
        logger.info("创建用户: {}", user.getUsername());
        try {
            User createdUser = userService.createUser(user);
            logger.info("用户创建成功: {}", createdUser.getUsername());
            return ApiResponse.success("用户创建成功", createdUser);
        } catch (RuntimeException e) {
            logger.error("用户创建失败: {}", e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        logger.info("更新用户: {}", id);
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ApiResponse.success("用户更新成功", updatedUser);
        } catch (RuntimeException e) {
            logger.error("用户更新失败: {}", e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable Long id) {
        logger.info("删除用户: {}", id);
        try {
            userService.deleteUser(id);
            return ApiResponse.success("用户删除成功", null);
        } catch (Exception e) {
            logger.error("删除用户失败: {}", e.getMessage());
            return ApiResponse.error("删除用户失败");
        }
    }

    @PostMapping("/login")
    public ApiResponse<User> login(@RequestBody LoginRequest loginRequest) {
        logger.info("用户登录尝试: {}", loginRequest.getUsername());
        try {
            Optional<User> user = userService.validateLogin(loginRequest.getUsername(), loginRequest.getPassword());
            if (user.isPresent()) {
                logger.info("用户登录成功: {}", loginRequest.getUsername());
                return ApiResponse.success("登录成功", user.get());
            } else {
                logger.warn("用户登录失败: {}", loginRequest.getUsername());
                return ApiResponse.error("用户名或密码错误");
            }
        } catch (Exception e) {
            logger.error("登录过程发生错误: {}", e.getMessage());
            return ApiResponse.error("登录失败，请稍后重试");
        }
    }
}