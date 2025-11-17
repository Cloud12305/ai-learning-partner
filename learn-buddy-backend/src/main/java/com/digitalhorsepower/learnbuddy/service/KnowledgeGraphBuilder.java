// KnowledgeGraphBuilder.java
package com.digitalhorsepower.learnbuddy.service;

import com.digitalhorsepower.learnbuddy.dto.QAResponse;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KnowledgeGraphBuilder {

    /**
     * 从AI回答内容中提取知识点并构建图谱
     */
    public QAResponse.KnowledgeGraphData buildGraphFromAnswer(String question, String answer) {
        QAResponse.KnowledgeGraphData graphData = new QAResponse.KnowledgeGraphData();

        // 1. 提取知识点
        List<String> knowledgePoints = extractKnowledgePoints(question, answer);

        // 2. 构建节点
        List<QAResponse.GraphNode> nodes = buildNodes(knowledgePoints, question);

        // 3. 构建关系
        List<QAResponse.GraphLink> links = buildLinks(nodes, question);

        // 4. 设置布局
        Map<String, Object> layout = buildLayout();

        graphData.setNodes(nodes);
        graphData.setLinks(links);
        graphData.setLayout(layout);

        return graphData;
    }

    private List<String> extractKnowledgePoints(String question, String answer) {
        List<String> points = new ArrayList<>();

        // 从问题中提取主主题
        String mainTopic = extractMainTopic(question);
        points.add(mainTopic);

        // 从回答中提取关键词
        points.addAll(extractKeywordsFromAnswer(answer));

        // 添加相关概念
        points.addAll(getRelatedConcepts(mainTopic));

        return points.stream().distinct().collect(Collectors.toList());
    }

    private List<QAResponse.GraphNode> buildNodes(List<String> knowledgePoints, String question) {
        List<QAResponse.GraphNode> nodes = new ArrayList<>();
        String mainTopic = extractMainTopic(question);

        for (String point : knowledgePoints) {
            QAResponse.GraphNode node = new QAResponse.GraphNode();
            node.setId(generateNodeId(point));
            node.setName(point);
            node.setCategory(classifyCategory(point));
            node.setDefinition(generateDefinition(point));
            node.setSymbolSize(calculateNodeSize(point, mainTopic));
            node.setItemStyle(buildNodeStyle(point, mainTopic));
            node.setLabel(buildLabelStyle());

            nodes.add(node);
        }

        return nodes;
    }

    private List<QAResponse.GraphLink> buildLinks(List<QAResponse.GraphNode> nodes, String question) {
        List<QAResponse.GraphLink> links = new ArrayList<>();
        // 移除了未使用的 mainTopic 变量

        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                QAResponse.GraphNode source = nodes.get(i);
                QAResponse.GraphNode target = nodes.get(j);

                if (shouldCreateLink(source, target)) {
                    QAResponse.GraphLink link = new QAResponse.GraphLink();
                    link.setSource(source.getId());
                    link.setTarget(target.getId());
                    link.setRelation(determineRelation(source, target));
                    link.setDescription(generateLinkDescription(source, target));
                    link.setLineStyle(buildLinkStyle());

                    links.add(link);
                }
            }
        }

        return links;
    }

    // 辅助方法
    private String extractMainTopic(String question) {
        if (question.contains("二叉树")) return "二叉树";
        if (question.contains("链表") && question.contains("数组")) return "链表与数组";
        if (question.contains("快速排序")) return "快速排序";
        if (question.contains("KMP") || question.contains("kmp")) return "KMP算法";
        if (question.contains("网络")) return "计算机网络";
        if (question.contains("数据库")) return "数据库";

        return question.replace("什么是", "").replace("？", "").trim();
    }

    private List<String> extractKeywordsFromAnswer(String answer) {
        List<String> keywords = new ArrayList<>();

        // 简单的关键词提取逻辑
        String[] commonKeywords = {"算法", "数据结构", "时间复杂度", "空间复杂度", "递归",
                "分治", "动态规划", "树", "图", "链表", "数组", "栈", "队列",
                "TCP", "UDP", "HTTP", "协议", "数据库", "索引", "事务"};

        for (String keyword : commonKeywords) {
            if (answer.contains(keyword)) {
                keywords.add(keyword);
            }
        }

        return keywords;
    }

    private List<String> getRelatedConcepts(String mainTopic) {
        Map<String, List<String>> conceptMap = new HashMap<>();
        conceptMap.put("二叉树", Arrays.asList("树结构", "遍历算法", "二叉搜索树", "平衡二叉树"));
        conceptMap.put("KMP算法", Arrays.asList("字符串匹配", "模式匹配", "部分匹配表", "next数组"));
        conceptMap.put("快速排序", Arrays.asList("排序算法", "分治策略", "递归", "分区操作"));
        conceptMap.put("计算机网络", Arrays.asList("OSI模型", "TCP/IP协议", "HTTP协议", "路由器"));

        return conceptMap.getOrDefault(mainTopic,
                Arrays.asList("基础概念", "核心原理", "应用场景", "相关技术"));
    }

    private String generateNodeId(String point) {
        return "node_" + Math.abs(point.hashCode());
    }

    private Integer calculateNodeSize(String point, String mainTopic) {
        if (point.equals(mainTopic)) return 50;
        if (getRelatedConcepts(mainTopic).contains(point)) return 30;
        return 20;
    }

    private Map<String, Object> buildNodeStyle(String point, String mainTopic) {
        Map<String, Object> style = new HashMap<>();

        if (point.equals(mainTopic)) {
            style.put("color", "#5470c6");
        } else if (getRelatedConcepts(mainTopic).contains(point)) {
            style.put("color", "#91cc75");
        } else {
            style.put("color", "#fac858");
        }

        return style;
    }

    private Map<String, Object> buildLabelStyle() {
        Map<String, Object> labelStyle = new HashMap<>();
        labelStyle.put("show", true);
        labelStyle.put("fontSize", 12);
        labelStyle.put("color", "#333");
        return labelStyle;
    }

    private boolean shouldCreateLink(QAResponse.GraphNode source, QAResponse.GraphNode target) {
        // 简单的链接创建逻辑
        String sourceCat = source.getCategory();
        String targetCat = target.getCategory();

        return sourceCat.equals(targetCat) ||
                Math.abs(source.getName().hashCode() % 3) == 0;
    }

    private String determineRelation(QAResponse.GraphNode source, QAResponse.GraphNode target) {
        // 根据节点关系确定关系类型
        if (source.getCategory().equals(target.getCategory())) {
            return "同类关系";
        } else if (source.getName().contains("算法") && target.getName().contains("应用")) {
            return "应用关系";
        } else if (source.getName().contains("基础") || target.getName().contains("基础")) {
            return "基础关系";
        } else {
            return "相关关系";
        }
    }

    private String generateLinkDescription(QAResponse.GraphNode source, QAResponse.GraphNode target) {
        return source.getName() + "与" + target.getName() + "的关联关系";
    }

    private Map<String, Object> buildLinkStyle() {
        Map<String, Object> lineStyle = new HashMap<>();
        lineStyle.put("color", "#aaa");
        lineStyle.put("width", 1);
        lineStyle.put("curveness", 0.2);
        return lineStyle;
    }

    private String generateDefinition(String point) {
        // 为常见知识点生成定义
        Map<String, String> definitions = new HashMap<>();
        definitions.put("二叉树", "每个节点最多有两个子节点的树结构，是计算机科学中重要的数据结构");
        definitions.put("KMP算法", "高效的字符串匹配算法，通过部分匹配表避免重复比较");
        definitions.put("快速排序", "基于分治策略的高效排序算法，平均时间复杂度为O(n log n)");
        definitions.put("计算机网络", "连接多台计算机实现资源共享和信息传递的系统");
        definitions.put("链表与数组", "两种基本数据结构，分别基于指针链接和连续内存存储");
        definitions.put("排序算法", "将数据元素按特定顺序排列的算法");
        definitions.put("树结构", "由n个有限节点组成的有层次关系的集合");
        definitions.put("遍历算法", "按照某种规则访问数据结构中所有节点的算法");
        definitions.put("字符串匹配", "在文本中查找特定模式串的过程");
        definitions.put("分治策略", "将复杂问题分解为多个相同或相似的子问题的算法思想");
        definitions.put("递归", "函数调用自身的编程技巧");
        definitions.put("OSI模型", "网络通信的七层参考模型，定义网络协议的标准框架");
        definitions.put("TCP/IP协议", "互联网的基础通信协议套件");
        definitions.put("HTTP协议", "应用层协议，用于分布式、协作式和超媒体信息系统的应用");

        return definitions.getOrDefault(point,
                point + "是计算机科学中的重要概念，在" + classifyCategory(point) + "领域有广泛应用");
    }

    private Map<String, Object> buildLayout() {
        Map<String, Object> layout = new HashMap<>();
        layout.put("type", "force");
        layout.put("repulsion", 100);
        layout.put("gravity", 0.1);
        layout.put("edgeLength", 100);
        return layout;
    }

    private String classifyCategory(String point) {
        if (point.contains("树") || point.contains("链表") || point.contains("数组") ||
                point.contains("队列") || point.contains("栈")) {
            return "数据结构";
        }
        if (point.contains("算法") || point.contains("排序") || point.contains("复杂度") ||
                point.contains("KMP") || point.contains("匹配")) {
            return "算法";
        }
        if (point.contains("TCP") || point.contains("UDP") || point.contains("网络") ||
                point.contains("协议") || point.contains("HTTP") || point.contains("OSI")) {
            return "网络";
        }
        if (point.contains("数据库") || point.contains("索引") || point.contains("SQL")) {
            return "数据库";
        }
        return "计算机基础";
    }
}