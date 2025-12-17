package com.digitalhorsepower.learnbuddy.controller;

import com.digitalhorsepower.learnbuddy.dto.BaseResponse;
import com.digitalhorsepower.learnbuddy.entity.User;
import com.digitalhorsepower.learnbuddy.service.UserService;
import com.digitalhorsepower.learnbuddy.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // ========== 公开接口（不需要认证） ==========

    // 用户登录
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<Map<String, Object>>> login(@RequestBody LoginRequest loginRequest) {
        try {
            logger.info("用户登录请求: {}", loginRequest.getUsername());

            // 验证用户逻辑
            User user = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

            if (user != null) {
                // 生成 token
                String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

                logger.info("用户登录成功: {}", user.getUsername());

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

                // 构建响应数据
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("token", token);
                responseData.put("user", userInfo);

                return ResponseEntity.ok(BaseResponse.success("登录成功", responseData));
            } else {
                logger.warn("登录失败: 用户名或密码错误");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(BaseResponse.error("用户名或密码错误"));
            }
        } catch (Exception e) {
            logger.error("登录过程发生错误: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.error("登录失败，请稍后重试"));
        }
    }

    // 用户注册
    @PostMapping("/register")
    public BaseResponse<User> register(@RequestBody User user) {
        logger.info("用户注册尝试: {}", user.getUsername());
        try {
            User createdUser = userService.createUser(user);
            logger.info("用户注册成功: {}", createdUser.getUsername());
            return BaseResponse.success("用户注册成功", createdUser);
        } catch (RuntimeException e) {
            logger.error("用户注册失败: {}", e.getMessage());
            return BaseResponse.error(e.getMessage());
        }
    }

    // 健康检查接口
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "用户服务运行正常");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("service", "User Service");
        return ResponseEntity.ok(response);
    }

    // ========== 需要认证的接口 ==========

    // 更新学习目标 - 改为完全不同的路径避免冲突
    @PutMapping("/set-learning-goal")
    public BaseResponse<String> updateLearningGoal(@RequestBody Map<String, String> request,
                                                   @RequestAttribute Long userId) {
        try {
            logger.info("更新学习目标请求 - 用户ID: {}", userId);

            // 添加调试日志
            logger.info("请求路径: /set-learning-goal");
            logger.info("请求体: {}", request);

            String learningGoal = request.get("learningGoal");
            if (learningGoal == null || learningGoal.trim().isEmpty()) {
                return BaseResponse.error("学习目标不能为空");
            }

            // 调用服务层更新学习目标
            boolean updated = userService.updateLearningGoal(userId, learningGoal);

            if (updated) {
                logger.info("学习目标更新成功 - 用户ID: {}, 目标: {}", userId, learningGoal);
                return BaseResponse.success("学习目标更新成功");
            } else {
                logger.warn("学习目标更新失败 - 用户ID: {}", userId);
                return BaseResponse.error("更新学习目标失败");
            }
        } catch (Exception e) {
            logger.error("更新学习目标异常 - 用户ID: {}, 错误: {}", userId, e.getMessage(), e);
            return BaseResponse.error("更新学习目标时发生错误");
        }
    }

    // 获取当前用户信息
    @GetMapping("/me")
    public BaseResponse<User> getCurrentUser(@RequestAttribute Long userId) {
        logger.info("获取当前用户信息: {}", userId);
        Optional<User> user = userService.getUserById(userId);
        return user.map(value -> BaseResponse.success("获取用户信息成功", value))
                .orElse(BaseResponse.error("用户不存在"));
    }

    // 获取所有用户（仅管理员）
    @GetMapping
    public BaseResponse<List<User>> getAllUsers(@RequestAttribute String userRole) {
        if (!"ADMIN".equals(userRole)) {
            return BaseResponse.error("权限不足，仅管理员可查看所有用户");
        }

        logger.info("获取所有用户列表");
        List<User> users = userService.getAllUsers();
        return BaseResponse.success("获取用户列表成功", users);
    }

    // 根据ID获取用户（学生只能查看自己，管理员可查看所有）
    @GetMapping("/{id}")
    public BaseResponse<User> getUserById(@PathVariable Long id,
                                          @RequestAttribute Long userId,
                                          @RequestAttribute String userRole) {
        // 学生只能查看自己的信息
        if ("STUDENT".equals(userRole) && !userId.equals(id)) {
            return BaseResponse.error("权限不足，只能查看自己的信息");
        }

        logger.info("根据ID获取用户: {}", id);
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> BaseResponse.success("获取用户成功", value))
                .orElse(BaseResponse.error("用户不存在"));
    }

    // 根据用户名获取用户（仅管理员）
    @GetMapping("/username/{username}")
    public BaseResponse<User> getUserByUsername(@PathVariable String username,
                                                @RequestAttribute String userRole) {
        if (!"ADMIN".equals(userRole)) {
            return BaseResponse.error("权限不足，仅管理员可操作");
        }

        logger.info("根据用户名获取用户: {}", username);
        Optional<User> user = userService.getUserByUsername(username);
        return user.map(value -> BaseResponse.success("获取用户成功", value))
                .orElse(BaseResponse.error("用户不存在"));
    }

    // 根据学号获取用户（学生只能查看自己，管理员可查看所有）
    @GetMapping("/student/{studentId}")
    public BaseResponse<User> getUserByStudentId(@PathVariable String studentId,
                                                 @RequestAttribute Long userId,
                                                 @RequestAttribute String userRole) {
        // 获取当前用户信息
        Optional<User> currentUser = userService.getUserById(userId);
        if (currentUser.isEmpty()) {
            return BaseResponse.error("用户不存在");
        }

        // 学生只能查看自己的信息
        if ("STUDENT".equals(userRole) && !currentUser.get().getStudentId().equals(studentId)) {
            return BaseResponse.error("权限不足，只能查看自己的信息");
        }

        logger.info("根据学号获取用户: {}", studentId);
        Optional<User> user = userService.getUserByStudentId(studentId);
        return user.map(value -> BaseResponse.success("获取用户成功", value))
                .orElse(BaseResponse.error("用户不存在"));
    }

    // 创建用户（仅管理员）
    @PostMapping("/create")
    public BaseResponse<User> createUser(@RequestBody User user,
                                         @RequestAttribute String userRole) {
        if (!"ADMIN".equals(userRole)) {
            return BaseResponse.error("权限不足，仅管理员可创建用户");
        }

        logger.info("创建用户: {}", user.getUsername());
        try {
            User createdUser = userService.createUser(user);
            logger.info("用户创建成功: {}", createdUser.getUsername());
            return BaseResponse.success("用户创建成功", createdUser);
        } catch (RuntimeException e) {
            logger.error("用户创建失败: {}", e.getMessage());
            return BaseResponse.error(e.getMessage());
        }
    }

    // 创建管理员用户（仅管理员）
    @PostMapping("/admin")
    public BaseResponse<User> createAdminUser(@RequestBody User user,
                                              @RequestAttribute String userRole) {
        if (!"ADMIN".equals(userRole)) {
            return BaseResponse.error("权限不足，仅管理员可创建管理员用户");
        }

        logger.info("创建管理员用户: {}", user.getUsername());
        try {
            User createdUser = userService.createAdminUser(user);
            logger.info("管理员用户创建成功: {}", createdUser.getUsername());
            return BaseResponse.success("管理员用户创建成功", createdUser);
        } catch (RuntimeException e) {
            logger.error("管理员用户创建失败: {}", e.getMessage());
            return BaseResponse.error(e.getMessage());
        }
    }

    // 更新用户信息（学生只能更新自己，管理员可更新所有）
    @PutMapping("/{id}")
    public BaseResponse<User> updateUser(@PathVariable Long id,
                                         @RequestBody User userDetails,
                                         @RequestAttribute Long userId,
                                         @RequestAttribute String userRole) {
        // 学生只能更新自己的信息
        if ("STUDENT".equals(userRole) && !userId.equals(id)) {
            return BaseResponse.error("权限不足，只能更新自己的信息");
        }

        logger.info("更新用户: {}", id);
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return BaseResponse.success("用户更新成功", updatedUser);
        } catch (RuntimeException e) {
            logger.error("用户更新失败: {}", e.getMessage());
            return BaseResponse.error(e.getMessage());
        }
    }

    // 更新用户角色（仅管理员）
    @PutMapping("/{id}/role")
    public BaseResponse<User> updateUserRole(@PathVariable Long id,
                                             @RequestBody Map<String, String> request,
                                             @RequestAttribute String userRole) {
        if (!"ADMIN".equals(userRole)) {
            return BaseResponse.error("权限不足，仅管理员可修改用户角色");
        }

        String role = request.get("role");
        if (role == null || (!"STUDENT".equals(role) && !"ADMIN".equals(role))) {
            return BaseResponse.error("角色必须是 STUDENT 或 ADMIN");
        }

        logger.info("更新用户角色: {} -> {}", id, role);
        try {
            User updatedUser = userService.updateUserRole(id, role);
            return BaseResponse.success("用户角色更新成功", updatedUser);
        } catch (RuntimeException e) {
            logger.error("用户角色更新失败: {}", e.getMessage());
            return BaseResponse.error(e.getMessage());
        }
    }

    // 删除用户（仅管理员）
    @DeleteMapping("/{id}")
    public BaseResponse<String> deleteUser(@PathVariable Long id,
                                           @RequestAttribute String userRole) {
        if (!"ADMIN".equals(userRole)) {
            return BaseResponse.error("权限不足，仅管理员可删除用户");
        }

        logger.info("删除用户: {}", id);
        try {
            userService.deleteUser(id);
            return BaseResponse.success("用户删除成功");
        } catch (Exception e) {
            logger.error("删除用户失败: {}", e.getMessage());
            return BaseResponse.error("删除用户失败");
        }
    }

    // 获取所有管理员用户（仅管理员）
    @GetMapping("/admins")
    public BaseResponse<List<User>> getAdminUsers(@RequestAttribute String userRole) {
        if (!"ADMIN".equals(userRole)) {
            return BaseResponse.error("权限不足，仅管理员可查看管理员列表");
        }

        logger.info("获取所有管理员用户");
        List<User> admins = userService.getAdminUsers();
        return BaseResponse.success("获取管理员列表成功", admins);
    }

    // 获取所有学生用户（仅管理员）
    @GetMapping("/students")
    public BaseResponse<List<User>> getStudentUsers(@RequestAttribute String userRole) {
        if (!"ADMIN".equals(userRole)) {
            return BaseResponse.error("权限不足，仅管理员可查看学生列表");
        }

        logger.info("获取所有学生用户");
        List<User> students = userService.getStudentUsers();
        return BaseResponse.success("获取学生列表成功", students);
    }

    // 检查权限（内部使用）
    @GetMapping("/check-permission")
    public BaseResponse<Map<String, Object>> checkPermission(@RequestAttribute Long userId,
                                                             @RequestAttribute String userRole) {
        Map<String, Object> permissionInfo = new HashMap<>();
        permissionInfo.put("userId", userId);
        permissionInfo.put("role", userRole);
        permissionInfo.put("isAdmin", "ADMIN".equals(userRole));
        permissionInfo.put("isStudent", "STUDENT".equals(userRole));

        return BaseResponse.success("权限检查成功", permissionInfo);
    }
    // 学生更新自己的信息（不检查权限，直接使用当前用户ID）
    @PutMapping("/me/update")
    public BaseResponse<User> updateCurrentUser(@RequestBody User userDetails,
                                                @RequestAttribute Long userId) {
        try {
            logger.info("用户更新自己的信息: {}", userId);
            User updatedUser = userService.updateUser(userId, userDetails);
            return BaseResponse.success("用户信息更新成功", updatedUser);
        } catch (RuntimeException e) {
            logger.error("用户更新失败: {}", e.getMessage());
            return BaseResponse.error(e.getMessage());
        } catch (Exception e) {
            logger.error("更新用户异常: {}", e.getMessage());
            return BaseResponse.error("更新用户失败");
        }
    }
    // 内部类：登录请求DTO
    public static class LoginRequest {
        private String username;
        private String password;

        // Getter 和 Setter
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}