// KnowledgeRelation.java
package com.digitalhorsepower.learnbuddy.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "knowledge_relations")
public class KnowledgeRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kp1_id", nullable = false)
    private KnowledgePoint kp1;

    @ManyToOne
    @JoinColumn(name = "kp2_id", nullable = false)
    private KnowledgePoint kp2;

    @Column(nullable = false)
    private String relationType;

    private String description;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}