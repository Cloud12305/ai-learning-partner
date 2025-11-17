// KnowledgeRelationRepository.java
package com.digitalhorsepower.learnbuddy.repository;

import com.digitalhorsepower.learnbuddy.entity.KnowledgeRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KnowledgeRelationRepository extends JpaRepository<KnowledgeRelation, Long> {
    @Query("SELECT kr FROM KnowledgeRelation kr WHERE kr.kp1.kpName = :kpName OR kr.kp2.kpName = :kpName")
    List<KnowledgeRelation> findRelationsByKpName(@Param("kpName") String kpName);

    List<KnowledgeRelation> findByRelationType(String relationType);
}