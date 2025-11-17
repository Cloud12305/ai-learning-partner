// QAResponse.java
package com.digitalhorsepower.learnbuddy.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class QAResponse {
    private boolean success;
    private String answer;
    private List<KnowledgeNode> knowledgeGraph;
    private String lastContextKp;
    private String timestamp;

    @Data
    public static class KnowledgeNode {
        private String id;
        private String name;
        private String definition;
        private String category;
        private List<KnowledgeLink> links;
    }

    @Data
    public static class KnowledgeLink {
        private String source;
        private String target;
        private String relation;
        private String description;
    }
}