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