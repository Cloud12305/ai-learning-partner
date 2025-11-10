package com.digitalhorsepower.learnbuddy.service;

import com.digitalhorsepower.learnbuddy.entity.User;
import com.digitalhorsepower.learnbuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByStudentId(String studentId) {
        return userRepository.findByStudentId(studentId);
    }

    // 新增方法：支持用户名或学号查找
    private Optional<User> findUserByIdentifier(String identifier) {
        // 先尝试按用户名查找
        Optional<User> user = userRepository.findByUsername(identifier);
        if (user.isPresent()) {
            return user;
        }

        // 再尝试按学号查找
        return userRepository.findByStudentId(identifier);
    }
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

        return userRepository.save(user);
    }

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
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

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
                userRepository.save(user);
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}