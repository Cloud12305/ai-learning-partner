package com.digitalhorsepower.learnbuddy.repository;

import com.digitalhorsepower.learnbuddy.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    Optional<UserRole> findByUserId(Long userId);

    @Query("SELECT ur.role FROM UserRole ur WHERE ur.user.id = :userId")
    Optional<String> findRoleByUserId(@Param("userId") Long userId);

    boolean existsByUserIdAndRole(Long userId, String role);

    @Query("SELECT ur.user.id FROM UserRole ur WHERE ur.role = :role")
    List<Long> findUserIdsByRole(@Param("role") String role);

    @Query("SELECT COUNT(ur) FROM UserRole ur WHERE ur.role = :role")
    long countByRole(@Param("role") String role);
}