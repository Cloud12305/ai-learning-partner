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

    public User createUser(User user) {
        // 简单的校验逻辑
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        if (user.getEmail() != null && userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }

        // 设置默认值
        if (user.getStudentId() == null || user.getStudentId().trim().isEmpty()) {
            // 生成基于时间的临时学号
            user.setStudentId("TEMP_" + System.currentTimeMillis());
        }

        if (user.getCollege() == null) {
            user.setCollege("计算机学院");
        }

        if (user.getGrade() == null) {
            user.setGrade("2023");
        }

        if (user.getMajor() == null) {
            user.setMajor("计算机科学与技术");
        }

        if (user.getClassName() == null) {
            user.setClassName("计科2301");
        }

        if (user.getAvatarUrl() == null) {
            user.setAvatarUrl("https://picsum.photos/100/100?random=" + (int)(Math.random() * 100));
        }

        if (user.getEnrollmentDate() == null) {
            user.setEnrollmentDate(LocalDateTime.now());
        }

        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            if (userDetails.getEmail() != null) {
                user.setEmail(userDetails.getEmail());
            }
            if (userDetails.getName() != null) {
                user.setName(userDetails.getName());
            }
            if (userDetails.getMajor() != null) {
                user.setMajor(userDetails.getMajor());
            }
            if (userDetails.getGrade() != null) {
                user.setGrade(userDetails.getGrade());
            }
            if (userDetails.getLearningGoal() != null) {
                user.setLearningGoal(userDetails.getLearningGoal());
            }
            if (userDetails.getCollege() != null) {
                user.setCollege(userDetails.getCollege());
            }
            if (userDetails.getClassName() != null) {
                user.setClassName(userDetails.getClassName());
            }
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // 添加登录验证方法，支持用户名和学号登录
    public Optional<User> validateLogin(String username, String password) {
        // 先尝试用用户名查找
        Optional<User> userOptional = userRepository.findByUsername(username);

        // 如果用户名找不到，尝试用学号查找
        if (!userOptional.isPresent()) {
            userOptional = userRepository.findByStudentId(username);
        }

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // 更新登录信息
            user.setLoginCount(user.getLoginCount() + 1);
            user.setLastLoginTime(LocalDateTime.now());
            userRepository.save(user);

            // 验证密码
            if (user.getPassword().equals(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}