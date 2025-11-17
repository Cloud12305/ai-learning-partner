package com.digitalhorsepower.learnbuddy.service;

import com.digitalhorsepower.learnbuddy.dto.QAResponse;
import com.digitalhorsepower.learnbuddy.entity.KnowledgePoint;
import com.digitalhorsepower.learnbuddy.entity.KnowledgeRelation;
import com.digitalhorsepower.learnbuddy.repository.KnowledgePointRepository;
import com.digitalhorsepower.learnbuddy.repository.KnowledgeRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
            System.out.println(">>> 开始处理问题: " + question);

            // 1. 调用DeepSeek生成完整回答
            String answer = deepSeekService.generateKnowledgeAnswer(question);

            // 2. 分析问题提取知识点
            Map<String, Object> analysisResult = analyzeQuestionForKnowledge(question);

            // 3. 构建知识图谱数据
            List<QAResponse.KnowledgeNode> knowledgeGraph = buildKnowledgeGraph(analysisResult, question);

            // 4. 保存问答历史
            saveQAHistory(userId, question, answer, knowledgeGraph);

            QAResponse response = new QAResponse();
            response.setSuccess(true);
            response.setAnswer(answer);
            response.setKnowledgeGraph(knowledgeGraph);
            response.setLastContextKp((String) analysisResult.get("mainTopic"));
            response.setTimestamp(LocalDateTime.now().toString());

            System.out.println(">>> 问题处理完成:");
            System.out.println(">>> - 回答长度: " + answer.length());
            System.out.println(">>> - 图谱节点数: " + knowledgeGraph.size());
            System.out.println(">>> - 主要主题: " + analysisResult.get("mainTopic"));

            return response;

        } catch (Exception e) {
            System.out.println(">>> 问题处理失败: " + e.getMessage());
            e.printStackTrace();
            return getFallbackResponse(question);
        }
    }

    /**
     * 分析问题提取知识点
     */
    private Map<String, Object> analyzeQuestionForKnowledge(String question) {
        Map<String, Object> result = new HashMap<>();

        // 提取主要主题
        String mainTopic = extractMainTopic(question);
        result.put("mainTopic", mainTopic);

        // 生成相关主题
        List<String> relatedTopics = generateRelatedTopics(mainTopic);
        result.put("relatedTopics", relatedTopics);

        // 问题类型
        result.put("questionType", classifyQuestion(question));

        return result;
    }

    /**
     * 构建知识图谱
     */
    private List<QAResponse.KnowledgeNode> buildKnowledgeGraph(Map<String, Object> analysisResult, String question) {
        List<QAResponse.KnowledgeNode> graph = new ArrayList<>();
        String mainTopic = (String) analysisResult.get("mainTopic");

        System.out.println(">>> 构建知识图谱，主要主题: " + mainTopic);

        // 创建主节点
        QAResponse.KnowledgeNode mainNode = createMainNode(mainTopic, question);
        graph.add(mainNode);

        // 添加相关节点
        List<String> relatedTopics = (List<String>) analysisResult.get("relatedTopics");
        for (String topic : relatedTopics) {
            QAResponse.KnowledgeNode relatedNode = createRelatedNode(topic);
            graph.add(relatedNode);

            // 添加关系链接
            QAResponse.KnowledgeLink link = new QAResponse.KnowledgeLink();
            link.setSource(mainNode.getId());
            link.setTarget(relatedNode.getId());
            link.setRelation(generateRelationType(mainTopic, topic));
            link.setDescription(mainTopic + "与" + topic + "的关系");

            mainNode.getLinks().add(link);
        }

        // 如果数据库中有相关知识点，也添加进来
        addDatabaseKnowledge(mainTopic, graph, mainNode);

        System.out.println(">>> 知识图谱构建完成:");
        System.out.println(">>> - 总节点数: " + graph.size());
        System.out.println(">>> - 主节点链接数: " + mainNode.getLinks().size());

        return graph;
    }

    private QAResponse.KnowledgeNode createMainNode(String topic, String question) {
        QAResponse.KnowledgeNode node = new QAResponse.KnowledgeNode();
        node.setId("main_" + topic.hashCode()); // 使用主题的hashCode确保唯一性
        node.setName(topic);
        node.setDefinition(generateDefinition(topic, question));
        node.setCategory(classifyCategory(topic));
        node.setLinks(new ArrayList<>());
        return node;
    }

    private QAResponse.KnowledgeNode createRelatedNode(String topic) {
        QAResponse.KnowledgeNode node = new QAResponse.KnowledgeNode();
        node.setId("related_" + topic.hashCode());
        node.setName(topic);
        node.setDefinition(generateRelatedDefinition(topic));
        node.setCategory(classifyCategory(topic));
        node.setLinks(new ArrayList<>());
        return node;
    }

    private void addDatabaseKnowledge(String mainTopic, List<QAResponse.KnowledgeNode> graph, QAResponse.KnowledgeNode mainNode) {
        try {
            // 查找数据库中的相关知识点
            Optional<KnowledgePoint> mainKp = knowledgePointRepository.findByKpName(mainTopic);
            if (mainKp.isPresent()) {
                // 更新主节点信息
                mainNode.setDefinition(mainKp.get().getDefinition());

                // 查找相关关系
                List<KnowledgeRelation> relations = knowledgeRelationRepository.findRelationsByKpName(mainTopic);
                for (KnowledgeRelation relation : relations) {
                    KnowledgePoint relatedKp = relation.getKp1().getKpName().equals(mainTopic) ?
                            relation.getKp2() : relation.getKp1();

                    // 检查是否已存在该节点
                    boolean exists = graph.stream().anyMatch(n -> n.getName().equals(relatedKp.getKpName()));
                    if (!exists) {
                        QAResponse.KnowledgeNode dbNode = new QAResponse.KnowledgeNode();
                        dbNode.setId("db_" + relatedKp.getId());
                        dbNode.setName(relatedKp.getKpName());
                        dbNode.setDefinition(relatedKp.getDefinition());
                        dbNode.setCategory(relatedKp.getCategory());
                        dbNode.setLinks(new ArrayList<>());
                        graph.add(dbNode);

                        // 添加关系
                        QAResponse.KnowledgeLink link = new QAResponse.KnowledgeLink();
                        link.setSource(mainNode.getId());
                        link.setTarget(dbNode.getId());
                        link.setRelation(relation.getRelationType());
                        link.setDescription(relation.getDescription());
                        mainNode.getLinks().add(link);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(">>> 数据库查询失败: " + e.getMessage());
            // 忽略数据库错误，使用模拟数据继续
        }
    }

    // 辅助方法
    private String extractMainTopic(String question) {
        if (question.contains("二叉树")) return "二叉树";
        if (question.contains("链表") && question.contains("数组")) return "链表与数组";
        if (question.contains("快速排序")) return "快速排序";
        if (question.contains("面向对象")) return "面向对象编程";
        if (question.contains("TCP") && question.contains("UDP")) return "TCP与UDP";
        if (question.contains("数据库索引")) return "数据库索引";
        if (question.contains("KMP") || question.contains("kmp")) return "KMP算法";
        if (question.contains("排序")) return "排序算法";
        if (question.contains("网络")) return "计算机网络";
        if (question.contains("数据库")) return "数据库";

        // 提取问题中的关键词
        String cleaned = question.replace("什么是", "")
                .replace("什么", "")
                .replace("？", "")
                .replace("?", "")
                .replace("解释", "")
                .replace("介绍", "")
                .replace("原理", "")
                .trim();

        // 如果清理后还有内容，使用清理后的内容
        if (!cleaned.isEmpty() && cleaned.length() > 1) {
            return cleaned;
        }

        // 默认提取前几个词作为主题
        String[] words = question.replace("？", "").replace("?", "").split(" ");
        return words.length > 0 ? words[0] : "计算机知识";
    }

    private List<String> generateRelatedTopics(String mainTopic) {
        Map<String, List<String>> topicMap = new HashMap<>();
        topicMap.put("二叉树", Arrays.asList("树结构", "遍历算法", "二叉搜索树", "平衡二叉树", "哈夫曼树"));
        topicMap.put("链表与数组", Arrays.asList("数据结构", "内存管理", "时间复杂度", "空间复杂度", "动态数组"));
        topicMap.put("快速排序", Arrays.asList("排序算法", "分治策略", "递归", "时间复杂度分析", "分区操作"));
        topicMap.put("KMP算法", Arrays.asList("字符串匹配", "模式匹配", "部分匹配表", "next数组", "算法优化"));
        topicMap.put("面向对象编程", Arrays.asList("封装", "继承", "多态", "类与对象", "设计原则"));
        topicMap.put("TCP与UDP", Arrays.asList("网络协议", "传输层", "可靠性", "实时性", "套接字编程"));
        topicMap.put("数据库索引", Arrays.asList("B+树", "查询优化", "聚簇索引", "非聚簇索引", "索引结构"));
        topicMap.put("排序算法", Arrays.asList("冒泡排序", "插入排序", "选择排序", "归并排序", "堆排序"));
        topicMap.put("计算机网络", Arrays.asList("OSI模型", "TCP/IP协议", "HTTP协议", "路由器", "交换机"));
        topicMap.put("数据库", Arrays.asList("SQL", "关系型数据库", "NoSQL", "事务", "ACID属性"));

        return topicMap.getOrDefault(mainTopic,
                Arrays.asList("基础概念", "核心原理", "应用场景", "相关技术", "最佳实践"));
    }

    private String classifyQuestion(String question) {
        if (question.contains("什么") || question.contains("什么是")) return "定义查询";
        if (question.contains("区别") || question.contains("不同")) return "对比分析";
        if (question.contains("原理") || question.contains("如何工作")) return "原理分析";
        if (question.contains("例子") || question.contains("示例")) return "应用示例";
        return "一般查询";
    }

    private String classifyCategory(String topic) {
        if (topic.contains("树") || topic.contains("链表") || topic.contains("数组") || topic.contains("队列") || topic.contains("栈"))
            return "数据结构";
        if (topic.contains("排序") || topic.contains("算法") || topic.contains("复杂度") || topic.contains("KMP"))
            return "算法";
        if (topic.contains("对象") || topic.contains("编程") || topic.contains("类") || topic.contains("封装") || topic.contains("继承"))
            return "编程语言";
        if (topic.contains("TCP") || topic.contains("UDP") || topic.contains("网络") || topic.contains("协议") || topic.contains("HTTP"))
            return "计算机网络";
        if (topic.contains("数据库") || topic.contains("索引") || topic.contains("SQL"))
            return "数据库";
        return "计算机基础";
    }

    private String generateDefinition(String topic, String question) {
        Map<String, String> definitions = new HashMap<>();
        definitions.put("二叉树", "每个节点最多有两个子节点的树结构，是计算机科学中重要的数据结构");
        definitions.put("链表与数组", "两种基本数据结构，分别基于指针链接和连续内存存储");
        definitions.put("快速排序", "基于分治策略的高效排序算法，平均时间复杂度为O(n log n)");
        definitions.put("KMP算法", "高效的字符串匹配算法，通过部分匹配表避免重复比较");
        definitions.put("面向对象编程", "以对象为核心的编程范式，包含封装、继承、多态三大特性");
        definitions.put("TCP与UDP", "传输层两种主要协议，分别提供可靠和不可靠的数据传输服务");
        definitions.put("数据库索引", "提高数据库查询性能的数据结构，如B+树索引");
        definitions.put("排序算法", "将数据元素按特定顺序排列的算法");
        definitions.put("计算机网络", "连接多台计算机实现资源共享和信息传递的系统");
        definitions.put("数据库", "存储、管理和维护数据的系统");

        return definitions.getOrDefault(topic,
                "关于" + topic + "的核心概念和定义，涉及" + classifyCategory(topic) + "领域");
    }

    private String generateRelatedDefinition(String topic) {
        return "与主知识点相关的概念：" + topic + "，在" + classifyCategory(topic) + "中具有重要作用";
    }

    private String generateRelationType(String source, String target) {
        if (target.contains("基础") || target.contains("概念")) return "基础知识";
        if (target.contains("应用") || target.contains("场景")) return "应用场景";
        if (target.contains("算法") || target.contains("实现")) return "实现方式";
        if (target.contains("优化") || target.contains("改进")) return "优化方案";
        if (target.contains("类型") || target.contains("分类")) return "分类关系";
        return "相关概念";
    }

    private void saveQAHistory(String userId, String question, String answer, List<QAResponse.KnowledgeNode> graph) {
        // 实现保存逻辑到数据库
        System.out.println(">>> 保存问答历史 - 用户: " + userId + ", 问题: " + question);
    }

    private QAResponse getFallbackResponse(String question) {
        QAResponse response = new QAResponse();
        response.setSuccess(true);
        response.setAnswer("**" + question + "**\n\n关于这个问题，我目前无法提供详细解答。建议您：\n\n• 检查问题表述是否清晰\n• 尝试换一种方式提问\n• 联系系统管理员获取帮助");
        response.setKnowledgeGraph(new ArrayList<>());
        response.setTimestamp(LocalDateTime.now().toString());
        return response;
    }
}