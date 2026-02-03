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
    private KnowledgeGraphData knowledgeGraphData; // 新增字段
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

    @Data
    public static class KnowledgeGraphData {
        private List<GraphNode> nodes;
        private List<GraphLink> links;
        private Map<String, Object> layout;
    }

    @Data
    public static class GraphNode {
        private String id;
        private String name;
        private String category;
        private String definition;
        private Integer symbolSize;
        private Map<String, Object> itemStyle;
        private Map<String, Object> label;
    }

    @Data
    public static class GraphLink {
        private String source;
        private String target;
        private String relation;
        private String description;
        private Map<String, Object> lineStyle;
    }
}