package com.digitalhorsepower.learnbuddy.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

@Service
public class DeepSeekService {

    @Value("${deepseek.api.key:}")
    private String apiKey;

    @Value("${deepseek.api.url:https://api.deepseek.com/v1/chat/completions}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public DeepSeekService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 生成知识问答回答
     */
    public String generateKnowledgeAnswer(String question) {
        try {
            System.out.println(">>> 调用DeepSeek API生成回答，问题: " + question);

            // 如果API密钥为空，使用备选回答
            if (apiKey == null || apiKey.isEmpty()) {
                System.out.println(">>> API密钥为空，使用备选回答");
                return generateFallbackAnswer(question);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            // 构建专业的系统提示词
            String systemPrompt = "你是一个专业的计算机科学教育助手，专门回答编程、算法、数据结构、计算机网络、数据库等问题。\n\n" +
                    "请按照以下格式提供专业、准确、详细的回答：\n" +
                    "1. 首先用 **加粗** 标出核心概念名称\n" +
                    "2. 提供清晰准确的定义和解释\n" +
                    "3. 详细说明核心原理和工作机制\n" +
                    "4. 列出主要特点、优势和应用场景\n" +
                    "5. 如果适用，给出简单示例或代码\n" +
                    "6. 最后提供学习建议\n\n" +
                    "要求：\n" +
                    "- 回答要专业准确，避免错误\n" +
                    "- 结构清晰，层次分明\n" +
                    "- 字数在400-600字左右\n" +
                    "- 使用中文回答\n" +
                    "- 对于算法问题，要说明时间复杂度和空间复杂度";

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek-chat");
            requestBody.put("temperature", 0.3);
            requestBody.put("max_tokens", 1500);
            requestBody.put("stream", false);

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", systemPrompt));
            messages.add(Map.of("role", "user", "content", "请详细解答这个问题：" + question));

            requestBody.put("messages", messages);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            String answer = root.path("choices").get(0).path("message").path("content").asText();

            System.out.println(">>> DeepSeek API 回答生成成功，长度: " + answer.length());
            return answer;

        } catch (Exception e) {
            System.out.println(">>> DeepSeek API 调用失败: " + e.getMessage());
            e.printStackTrace();

            // 返回详细的备选回答
            return generateFallbackAnswer(question);
        }
    }

    private String generateFallbackAnswer(String question) {
        // 为常见问题提供详细的备选回答
        Map<String, String> detailedAnswers = new HashMap<>();

        detailedAnswers.put("什么是kmp算法",
                "**KMP算法**\n\n" +
                        "**定义：** KMP算法（Knuth-Morris-Pratt算法）是一种高效的字符串匹配算法，由D.E.Knuth、J.H.Morris和V.R.Pratt共同提出。\n\n" +
                        "**核心原理：**\n" +
                        "1. **利用已匹配信息**：当出现字符不匹配时，KMP算法不会回溯主串的指针，而是利用之前已经匹配的信息，将模式串向右滑动尽可能远的距离\n" +
                        "2. **部分匹配表**：通过预处理模式串，生成一个next数组（部分匹配表），记录模式串中每个位置的前缀和后缀的最长公共元素长度\n" +
                        "3. **避免重复比较**：通过next数组，算法知道在匹配失败时应该将模式串移动多少位，从而避免重复比较已知匹配的字符\n\n" +
                        "**算法步骤：**\n" +
                        "1. 预处理模式串，生成next数组\n" +
                        "2. 使用双指针在主串和模式串上进行匹配\n" +
                        "3. 当字符匹配时，两个指针都向前移动\n" +
                        "4. 当字符不匹配时，根据next数组移动模式串指针\n\n" +
                        "**时间复杂度：**\n" +
                        "- 预处理阶段：O(m)，其中m是模式串长度\n" +
                        "- 匹配阶段：O(n)，其中n是主串长度\n" +
                        "- 总体复杂度：O(n+m)\n\n" +
                        "**优势：**\n" +
                        "- 比暴力匹配算法更高效\n" +
                        "- 不需要回溯主串指针\n" +
                        "- 特别适合处理大量文本的字符串匹配\n\n" +
                        "**应用场景：**\n" +
                        "- 文本编辑器中的查找功能\n" +
                        "- DNA序列匹配\n" +
                        "- 网络安全中的模式检测\n" +
                        "- 编译器的词法分析");

        detailedAnswers.put("什么是二叉树",
                "**二叉树**\n\n" +
                        "**定义：** 二叉树是每个节点最多有两个子节点的树形数据结构，通常称为左子节点和右子节点。\n\n" +
                        "**基本特性：**\n" +
                        "1. 每个节点最多有两个子节点\n" +
                        "2. 左子节点和右子节点有顺序之分，次序不能任意颠倒\n" +
                        "3. 即使树中某节点只有一个子节点，也要区分它是左子节点还是右子节点\n\n" +
                        "**常见类型：**\n" +
                        "1. **满二叉树**：所有非叶子节点都有两个子节点，且所有叶子节点都在同一层\n" +
                        "2. **完全二叉树**：除最后一层外，其他层都是满的，且最后一层节点都靠左排列\n" +
                        "3. **二叉搜索树**：左子树上所有节点的值均小于根节点，右子树上所有节点的值均大于根节点\n" +
                        "4. **平衡二叉树**：任何节点的两个子树的高度差不超过1\n\n" +
                        "**遍历方式：**\n" +
                        "- 前序遍历：根→左→右\n" +
                        "- 中序遍历：左→根→右\n" +
                        "- 后序遍历：左→右→根\n" +
                        "- 层次遍历：按层遍历\n\n" +
                        "**应用场景：**\n" +
                        "- 文件系统的目录结构\n" +
                        "- 数据库索引（B树、B+树）\n" +
                        "- 表达式求值\n" +
                        "- 霍夫曼编码");

        detailedAnswers.put("快速排序原理",
                "**快速排序**\n\n" +
                        "**定义：** 快速排序是一种基于分治策略的高效排序算法，由Tony Hoare在1960年提出。\n\n" +
                        "**核心原理：**\n" +
                        "1. **分治策略**：将原问题分解为若干个规模更小但结构与原问题相似的子问题\n" +
                        "2. **分区操作**：选择一个基准元素，将数组分为两个子数组，小于基准的放在左边，大于基准的放在右边\n" +
                        "3. **递归排序**：递归地对两个子数组进行快速排序\n\n" +
                        "**算法步骤：**\n" +
                        "1. 从数列中挑出一个元素，称为\"基准\"\n" +
                        "2. 重新排序数列，所有比基准值小的元素摆放在基准前面，所有比基准值大的元素摆在基准后面\n" +
                        "3. 递归地把小于基准值元素的子数列和大于基准值元素的子数列排序\n\n" +
                        "**时间复杂度：**\n" +
                        "- 平均情况：O(n log n)\n" +
                        "- 最坏情况：O(n²) - 当数组已经有序时\n" +
                        "- 最好情况：O(n log n)\n\n" +
                        "**空间复杂度：** O(log n) - 递归调用栈的空间\n\n" +
                        "**优点：**\n" +
                        "- 原地排序，不需要额外的存储空间\n" +
                        "- 在实际应用中通常比其他O(n log n)算法更快\n" +
                        "- 缓存友好\n\n" +
                        "**优化策略：**\n" +
                        "- 三数取中法选择基准\n" +
                        "- 当子数组较小时切换到插入排序\n" +
                        "- 三向切分快速排序");

        // 如果问题在预定义答案中，返回详细回答；否则返回通用回答
        String lowerQuestion = question.toLowerCase();
        for (Map.Entry<String, String> entry : detailedAnswers.entrySet()) {
            if (lowerQuestion.contains(entry.getKey().toLowerCase().replace("什么是", "").replace("原理", ""))) {
                return entry.getValue();
            }
        }

        // 通用回答
        return "**" + question + "**\n\n" +
                "这是一个关于" + extractMainTopic(question) + "的重要概念。\n\n" +
                "**核心内容：**\n" +
                "• 基本定义和核心原理\n" +
                "• 关键特性和工作机制\n" +
                "• 实际应用场景和示例\n" +
                "• 相关的算法复杂度分析\n\n" +
                "**学习建议：**\n" +
                "建议通过实际编码练习和可视化工具来深入理解这个概念，同时参考权威的教材和文档。";
    }

    private String extractMainTopic(String question) {
        // 移除疑问词和标点
        String cleaned = question.replace("什么是", "")
                .replace("什么", "")
                .replace("？", "")
                .replace("?", "")
                .replace("解释", "")
                .replace("介绍", "")
                .replace("原理", "")
                .trim();
        return cleaned.isEmpty() ? "该知识点" : cleaned;
    }

    // 原有的情绪分析方法保持不变
    public Map<String, String> analyzeEmotion(String userText) {
        try {
            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            // 构建系统提示词
            String systemPrompt = "你是一位专业的心理陪伴助手，擅长情绪支持和鼓励。请分析用户的情绪状态，用温暖、共情的方式回应。\n\n" +
                    "请按照以下格式提供回应：\n" +
                    "1. 首先识别主要情绪类型：中性、积极、焦虑、挫败、压力、困惑\n" +
                    "2. 然后提供具体的鼓励和建议\n" +
                    "3. 保持温暖、支持性的语气\n" +
                    "4. 字数在200-300字左右";

            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek-chat");
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 500);

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", systemPrompt));
            messages.add(Map.of("role", "user", "content", "用户输入：" + userText));
            requestBody.put("messages", messages);

            // 发送请求
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

            // 解析响应
            JsonNode root = objectMapper.readTree(response.getBody());
            String aiResponse = root.path("choices").get(0).path("message").path("content").asText();

            // 提取情绪和鼓励内容
            return parseAIResponse(aiResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return getFallbackResponse();
        }
    }

    private Map<String, String> parseAIResponse(String aiResponse) {
        Map<String, String> result = new HashMap<>();

        // 简单的情绪关键词匹配
        String[] emotions = {"焦虑", "挫败", "积极", "压力", "困惑", "中性", "悲伤", "疲惫"};
        String detectedEmotion = "中性";

        for (String emotion : emotions) {
            if (aiResponse.contains(emotion)) {
                detectedEmotion = emotion;
                break;
            }
        }

        result.put("emotion", detectedEmotion);
        result.put("encouragement", aiResponse);

        return result;
    }

    private Map<String, String> getFallbackResponse() {
        return Map.of(
                "emotion", "中性",
                "encouragement", "我理解你现在可能需要倾诉。虽然暂时无法提供详细分析，但请记得：学习中的困难都是暂时的，保持耐心，给自己一些时间。如果需要，可以尝试深呼吸放松一下。"
        );
    }
}