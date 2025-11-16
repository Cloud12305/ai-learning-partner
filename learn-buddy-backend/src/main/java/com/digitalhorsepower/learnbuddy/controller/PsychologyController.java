package com.digitalhorsepower.learnbuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.digitalhorsepower.learnbuddy.dto.PsychologyRequest;
import com.digitalhorsepower.learnbuddy.dto.PsychologyResponse;
import com.digitalhorsepower.learnbuddy.service.PsychologyService;
import java.util.HashMap;
import java.util.Map;

/**
 * 心理学相关功能控制器
 * 处理情绪分析等心理学相关的HTTP请求
 */
@RestController // 标识这是一个RESTful Web服务控制器
@RequestMapping("/api/psychology") // 定义基础请求路径
public class PsychologyController {

    @Autowired // 自动注入PsychologyService依赖
    private PsychologyService psychologyService;

    /**
     * 情绪分析接口
     * POST请求，接收文本内容并分析其中的情绪
     *
     * @param request 包含待分析文本和用户ID的请求体
     * @return 包含分析结果或错误信息的响应实体
     */
    @PostMapping // 处理POST请求
    public ResponseEntity<?> analyzeEmotion(@RequestBody PsychologyRequest request) {
        // 打印请求日志，用于调试和监控
        System.out.println(">>> /api/psychology called with text = " + request.getText());

        try {
            // 调用服务层进行情绪分析处理
            PsychologyResponse response = psychologyService.processEmotionAnalysis(
                    request.getText(),    // 获取请求中的文本内容
                    request.getUserId()   // 获取请求中的用户ID
            );

            // 构建成功响应
            return ResponseEntity.ok().body(Map.of(
                    "success", true,  // 操作成功标志
                    "data", response  // 情绪分析结果数据
            ));

        } catch (Exception e) {
            // 处理异常情况，返回错误响应
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false, // 操作失败标志
                    "msg", "情绪分析服务暂时不可用，请稍后重试" // 错误提示信息
            ));
        }
    }
}