package com.digitalhorsepower.learnbuddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.digitalhorsepower.learnbuddy.dto.PsychologyResponse;
import java.util.Map;

@Service
public class PsychologyService {

    @Autowired
    private DeepSeekService deepSeekService;

    public PsychologyResponse processEmotionAnalysis(String text, String userId) {
        // 调用DeepSeek API分析情绪
        Map<String, String> analysisResult = deepSeekService.analyzeEmotion(text);

        // 这里可以添加情绪记录保存逻辑（如果需要）
        // emotionRecordRepository.save(new EmotionRecord(userId, text, analysisResult.get("emotion")));

        return new PsychologyResponse(
                analysisResult.get("emotion"),
                analysisResult.get("encouragement")
        );
    }
}