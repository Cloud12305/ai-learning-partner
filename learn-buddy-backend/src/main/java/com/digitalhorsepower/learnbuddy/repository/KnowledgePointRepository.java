// KnowledgePointRepository.java
package com.digitalhorsepower.learnbuddy.repository;

import com.digitalhorsepower.learnbuddy.entity.KnowledgePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface KnowledgePointRepository extends JpaRepository<KnowledgePoint, Long> {
    Optional<KnowledgePoint> findByKpName(String kpName);

    @Query("SELECT kp FROM KnowledgePoint kp WHERE kp.kpName LIKE %:keyword%")
    List<KnowledgePoint> findByKeyword(@Param("keyword") String keyword);

    List<KnowledgePoint> findByCategory(String category);
}