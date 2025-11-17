package com.digitalhorsepower.learnbuddy.service;

import com.digitalhorsepower.learnbuddy.entity.User;
import com.digitalhorsepower.learnbuddy.entity.UserRole;
import com.digitalhorsepower.learnbuddy.repository.UserRepository;
import com.digitalhorsepower.learnbuddy.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    // 添加 authenticate 方法 - 从数据库验证用户
    public User authenticate(String username, String password) {
        try {
            Optional<User> userOptional;

            // 判断是学号还是用户名登录
            if (username.matches("\\d+")) { // 如果是纯数字，尝试学号登录
                userOptional = userRepository.findByStudentId(username);
            } else { // 否则尝试用户名登录
                userOptional = userRepository.findByUsername(username);
            }

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // 检查账户状态
                if (!"ACTIVE".equals(user.getAccountStatus())) {
                    throw new RuntimeException("账户已被禁用");
                }

                // 验证密码
                if (user.getPassword().equals(password)) {
                    // 更新登录信息
                    user.setLoginCount(user.getLoginCount() != null ? user.getLoginCount() + 1 : 1);
                    user.setLastLoginTime(LocalDateTime.now());
                    User savedUser = userRepository.save(user);

                    // 设置角色信息
                    String role = getUserRole(savedUser.getId());
                    savedUser.setRole(role);

                    return savedUser;
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("认证过程中发生错误: " + e.getMessage());
        }
    }
    /**
     * 验证用户登录（原有方法）
     */
    @Transactional
    public Optional<User> validateLogin(String username, String password) {
        Optional<User> userOptional;

        // 判断是学号还是用户名登录
        if (username.matches("\\d+")) { // 如果是纯数字，尝试学号登录
            userOptional = userRepository.findByStudentId(username);
        } else { // 否则尝试用户名登录
            userOptional = userRepository.findByUsername(username);
        }

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // 检查账户状态
            if (!"ACTIVE".equals(user.getAccountStatus())) {
                throw new RuntimeException("账户已被禁用");
            }

            // 验证密码
            if (user.getPassword().equals(password)) {
                // 更新登录信息
                user.setLoginCount(user.getLoginCount() != null ? user.getLoginCount() + 1 : 1);
                user.setLastLoginTime(LocalDateTime.now());
                User savedUser = userRepository.save(user);

                // 设置角色信息
                String role = getUserRole(savedUser.getId());
                savedUser.setRole(role);

                return Optional.of(savedUser);
            }
        }
        return Optional.empty();
    }
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        // 为每个用户设置角色信息
        users.forEach(user -> {
            String role = getUserRole(user.getId());
            user.setRole(role);
        });
        return users;
    }

    public Optional<User> getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        userOptional.ifPresent(user -> {
            String role = getUserRole(user.getId());
            user.setRole(role);
        });
        return userOptional;
    }

    public Optional<User> getUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        userOptional.ifPresent(user -> {
            String role = getUserRole(user.getId());
            user.setRole(role);
        });
        return userOptional;
    }

    public Optional<User> getUserByStudentId(String studentId) {
        Optional<User> userOptional = userRepository.findByStudentId(studentId);
        userOptional.ifPresent(user -> {
            String role = getUserRole(user.getId());
            user.setRole(role);
        });
        return userOptional;
    }

    /**
     * 获取用户角色
     */
    public String getUserRole(Long userId) {
        return userRoleRepository.findRoleByUserId(userId)
                .orElse("STUDENT"); // 默认角色
    }

    /**
     * 检查用户是否为管理员
     */
    public boolean isAdmin(Long userId) {
        return "ADMIN".equals(getUserRole(userId));
    }

    /**
     * 检查用户是否为学生
     */
    public boolean isStudent(Long userId) {
        return "STUDENT".equals(getUserRole(userId));
    }

    /**
     * 设置用户角色
     */
    @Transactional
    public void setUserRole(Long userId, String role) {
        if (!"STUDENT".equals(role) && !"ADMIN".equals(role)) {
            throw new RuntimeException("角色只能是 STUDENT 或 ADMIN");
        }

        Optional<UserRole> existingRole = userRoleRepository.findByUserId(userId);
        if (existingRole.isPresent()) {
            // 更新现有角色
            UserRole userRole = existingRole.get();
            userRole.setRole(role); // 现在可以正常设置字符串
            userRoleRepository.save(userRole);
        } else {
            // 创建新角色
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role); // 现在可以正常设置字符串
            userRoleRepository.save(userRole);
        }
    }

    @Transactional
    public User createUser(User user) {
        // 校验必填字段
        if (user.getStudentId() == null || user.getStudentId().trim().isEmpty()) {
            throw new RuntimeException("学号不能为空");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new RuntimeException("姓名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new RuntimeException("密码不能为空");
        }
        if (user.getMajor() == null || user.getMajor().trim().isEmpty()) {
            throw new RuntimeException("专业不能为空");
        }
        if (user.getCollege() == null || user.getCollege().trim().isEmpty()) {
            throw new RuntimeException("学院不能为空");
        }
        if (user.getGrade() == null || user.getGrade().trim().isEmpty()) {
            throw new RuntimeException("年级不能为空");
        }

        // 检查唯一性约束
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        if (userRepository.existsByStudentId(user.getStudentId())) {
            throw new RuntimeException("学号已存在");
        }
        if (user.getEmail() != null && !user.getEmail().trim().isEmpty()
                && userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }

        // 只设置头像默认值，其他字段必须由前端提供
        if (user.getAvatarUrl() == null || user.getAvatarUrl().trim().isEmpty()) {
            user.setAvatarUrl("https://picsum.photos/100/100?random=" + (int)(Math.random() * 100));
        }

        // 保存用户
        User savedUser = userRepository.save(user);

        // 为新用户创建默认角色（学生）
        UserRole userRole = new UserRole();
        userRole.setUser(savedUser);
        userRole.setRole("STUDENT"); // 直接使用字符串
        userRoleRepository.save(userRole);

        // 设置返回用户的角色信息
        savedUser.setRole("STUDENT");
        return savedUser;
    }

    /**
     * 创建管理员用户
     */
    @Transactional
    public User createAdminUser(User user) {
        User savedUser = createUser(user);

        // 更新角色为管理员
        setUserRole(savedUser.getId(), "ADMIN");
        savedUser.setRole("ADMIN");

        return savedUser;
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            // 只更新允许修改的字段
            if (userDetails.getEmail() != null) {
                // 检查邮箱是否被其他用户使用
                if (!user.getEmail().equals(userDetails.getEmail())
                        && userRepository.existsByEmail(userDetails.getEmail())) {
                    throw new RuntimeException("邮箱已被其他用户使用");
                }
                user.setEmail(userDetails.getEmail());
            }
            if (userDetails.getName() != null && !userDetails.getName().trim().isEmpty()) {
                user.setName(userDetails.getName());
            }
            if (userDetails.getMajor() != null && !userDetails.getMajor().trim().isEmpty()) {
                user.setMajor(userDetails.getMajor());
            }
            if (userDetails.getGrade() != null && !userDetails.getGrade().trim().isEmpty()) {
                user.setGrade(userDetails.getGrade());
            }
            if (userDetails.getLearningGoal() != null) {
                user.setLearningGoal(userDetails.getLearningGoal());
            }
            if (userDetails.getCollege() != null && !userDetails.getCollege().trim().isEmpty()) {
                user.setCollege(userDetails.getCollege());
            }
            if (userDetails.getClassName() != null) {
                user.setClassName(userDetails.getClassName());
            }
            if (userDetails.getAvatarUrl() != null) {
                user.setAvatarUrl(userDetails.getAvatarUrl());
            }
            if (userDetails.getGender() != null) {
                user.setGender(userDetails.getGender());
            }
            if (userDetails.getPhone() != null) {
                user.setPhone(userDetails.getPhone());
            }

            User updatedUser = userRepository.save(user);

            // 设置角色信息
            String role = getUserRole(updatedUser.getId());
            updatedUser.setRole(role);

            return updatedUser;
        }).orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    /**
     * 更新用户角色
     */
    @Transactional
    public User updateUserRole(Long userId, String role) {
        setUserRole(userId, role);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setRole(role);

        return user;
    }

    @Transactional
    public void deleteUser(Long id) {
        // 先删除用户角色
        userRoleRepository.findByUserId(id).ifPresent(userRoleRepository::delete);
        // 再删除用户
        userRepository.deleteById(id);
    }

    /**
     * 获取所有管理员用户
     */
    public List<User> getAdminUsers() {
        List<Long> adminUserIds = userRoleRepository.findUserIdsByRole("ADMIN");
        List<User> admins = userRepository.findAllById(adminUserIds);
        admins.forEach(user -> user.setRole("ADMIN"));
        return admins;
    }

    /**
     * 获取所有学生用户
     */
    public List<User> getStudentUsers() {
        List<Long> studentUserIds = userRoleRepository.findUserIdsByRole("STUDENT");
        List<User> students = userRepository.findAllById(studentUserIds);
        students.forEach(user -> user.setRole("STUDENT"));
        return students;
    }

    /**
     * 检查用户是否有权限执行管理操作
     */
    public boolean hasAdminPermission(Long userId) {
        return isAdmin(userId);
    }
}