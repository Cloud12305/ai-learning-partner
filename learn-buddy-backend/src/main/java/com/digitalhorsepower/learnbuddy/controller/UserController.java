package com.digitalhorsepower.learnbuddy.controller;

import com.digitalhorsepower.learnbuddy.dto.ApiResponse;
import com.digitalhorsepower.learnbuddy.entity.User;
import com.digitalhorsepower.learnbuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ApiResponse<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ApiResponse.success("获取用户列表成功", users);
    }

    @GetMapping("/{id}")
    public ApiResponse<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> ApiResponse.success("获取用户成功", value))
                .orElse(ApiResponse.error("用户不存在"));
    }

    @GetMapping("/username/{username}")
    public ApiResponse<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);
        return user.map(value -> ApiResponse.success("获取用户成功", value))
                .orElse(ApiResponse.error("用户不存在"));
    }

    @PostMapping
    public ApiResponse<User> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ApiResponse.success("用户创建成功", createdUser);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ApiResponse.success("用户更新成功", updatedUser);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ApiResponse.success("用户删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error("删除用户失败");
        }
    }
}