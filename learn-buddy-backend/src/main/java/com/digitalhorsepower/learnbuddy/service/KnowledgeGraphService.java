// KnowledgeGraphService.java
package com.digitalhorsepower.learnbuddy.service;

import com.digitalhorsepower.learnbuddy.dto.QAResponse;
import com.digitalhorsepower.learnbuddy.entity.KnowledgePoint;
import com.digitalhorsepower.learnbuddy.entity.KnowledgeRelation;
import com.digitalhorsepower.learnbuddy.repository.KnowledgePointRepository;
import com.digitalhorsepower.learnbuddy.repository.KnowledgeRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KnowledgeGraphService {

    @Autowired
    private KnowledgePointRepository knowledgePointRepository;

    @Autowired
    private KnowledgeRelationRepository knowledgeRelationRepository;

    @Autowired
    private DeepSeekService deepSeekService;

    /**
     * 处理知识问答并生成知识图谱
     */
    public QAResponse processQuestion(String question, String userId, String contextKp) {
        try {
            // 1. 调用DeepSeek分析问题并提取知识点
            Map<String, Object> analysisResult = deepSeekService.analyzeKnowledgeQuestion(question, contextKp);

            // 2. 构建知识图谱数据
            List<QAResponse.KnowledgeNode> knowledgeGraph = buildKnowledgeGraph(analysisResult);

            // 3. 生成回答
            String answer = generateAnswer(analysisResult, knowledgeGraph);

            // 4. 保存到数据库
            saveQAHistory(userId, question, answer, knowledgeGraph);

            QAResponse response = new QAResponse();
            response.setSuccess(true);
            response.setAnswer(answer);
            response.setKnowledgeGraph(knowledgeGraph);
            response.setLastContextKp((String) analysisResult.get("mainTopic"));
            response.setTimestamp(java.time.LocalDateTime.now().toString());

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return getFallbackResponse();
        }
    }

    /**
     * 构建知识图谱
     */
    private List<QAResponse.KnowledgeNode> buildKnowledgeGraph(Map<String, Object> analysisResult) {
        List<QAResponse.KnowledgeNode> graph = new ArrayList<>();
        String mainTopic = (String) analysisResult.get("mainTopic");

        // 查找主要知识点
        Optional<KnowledgePoint> mainKp = knowledgePointRepository.findByKpName(mainTopic);
        if (mainKp.isPresent()) {
            // 添加主要节点
            QAResponse.KnowledgeNode mainNode = createKnowledgeNode(mainKp.get());

            // 查找相关关系
            List<KnowledgeRelation> relations = knowledgeRelationRepository.findRelationsByKpName(mainTopic);
            for (KnowledgeRelation relation : relations) {
                // 添加关联节点和链接
                KnowledgePoint relatedKp = relation.getKp1().getKpName().equals(mainTopic) ?
                        relation.getKp2() : relation.getKp1();

                QAResponse.KnowledgeNode relatedNode = createKnowledgeNode(relatedKp);
                if (graph.stream().noneMatch(node -> node.getId().equals(relatedNode.getId()))) {
                    graph.add(relatedNode);
                }

                // 添加链接到主节点
                QAResponse.KnowledgeLink link = new QAResponse.KnowledgeLink();
                link.setSource(mainNode.getId());
                link.setTarget(relatedNode.getId());
                link.setRelation(relation.getRelationType());
                link.setDescription(relation.getDescription());

                mainNode.getLinks().add(link);
            }

            graph.add(mainNode);
        }

        return graph;
    }

    private QAResponse.KnowledgeNode createKnowledgeNode(KnowledgePoint kp) {
        QAResponse.KnowledgeNode node = new QAResponse.KnowledgeNode();
        node.setId("kp_" + kp.getId());
        node.setName(kp.getKpName());
        node.setDefinition(kp.getDefinition());
        node.setCategory(kp.getCategory());
        node.setLinks(new ArrayList<>());
        return node;
    }

    private String generateAnswer(Map<String, Object> analysisResult, List<QAResponse.KnowledgeNode> graph) {
        StringBuilder answer = new StringBuilder();
        String mainTopic = (String) analysisResult.get("mainTopic");

        answer.append("**").append(mainTopic).append("**\n\n");

        // 添加定义
        Optional<KnowledgePoint> mainKp = knowledgePointRepository.findByKpName(mainTopic);
        if (mainKp.isPresent()) {
            answer.append(mainKp.get().getDefinition()).append("\n\n");
        }

        // 添加相关知识点
        if (!graph.isEmpty() && graph.get(0).getLinks() != null && !graph.get(0).getLinks().isEmpty()) {
            answer.append("**相关知识点：**\n");
            for (QAResponse.KnowledgeLink link : graph.get(0).getLinks()) {
                String targetName = graph.stream()
                        .filter(node -> node.getId().equals(link.getTarget()))
                        .findFirst()
                        .map(QAResponse.KnowledgeNode::getName)
                        .orElse("");
                answer.append("• ").append(targetName).append("（").append(link.getRelation()).append("）\n");
            }
        }

        return answer.toString();
    }

    private void saveQAHistory(String userId, String question, String answer, List<QAResponse.KnowledgeNode> graph) {
        // 实现保存逻辑
    }

    private QAResponse getFallbackResponse() {
        QAResponse response = new QAResponse();
        response.setSuccess(false);
        response.setAnswer("抱歉，暂时无法处理您的提问，请稍后重试。");
        response.setKnowledgeGraph(new ArrayList<>());
        return response;
    }
}