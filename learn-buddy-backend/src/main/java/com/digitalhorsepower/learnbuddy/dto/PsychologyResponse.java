package com.digitalhorsepower.learnbuddy.dto;

/**
 * 心理学分析响应数据传输对象 (DTO)
 * 用于封装情绪分析服务的返回结果，包含情绪识别和鼓励建议信息
 * 作为API响应的一部分返回给客户端
 */
public class PsychologyResponse {

    /**
     * 识别出的情绪类型
     * 例如：happy（开心）、sad（悲伤）、angry（愤怒）、neutral（平静）等
     */
    private String emotion;

    /**
     * 根据情绪生成的鼓励或建议信息
     * 针对识别出的情绪提供相应的心理支持和建议
     */
    private String encouragement;

    /**
     * 响应生成的时间戳
     * 记录分析完成的具体时间，格式为ISO-8601标准时间字符串
     */
    private String timestamp;

    // 构造器

    /**
     * 默认无参构造器
     * Spring框架在反序列化时会使用此构造器
     */
    public PsychologyResponse() {}

    /**
     * 带参构造器
     * 用于快速创建响应对象，自动生成当前时间戳
     *
     * @param emotion 识别出的情绪类型
     * @param encouragement 对应的鼓励信息
     */
    public PsychologyResponse(String emotion, String encouragement) {
        this.emotion = emotion;
        this.encouragement = encouragement;
        // 自动设置当前时间作为时间戳
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    // getter 和 setter 方法

    /**
     * 获取情绪类型
     * @return 情绪类型字符串
     */
    public String getEmotion() {
        return emotion;
    }

    /**
     * 设置情绪类型
     * @param emotion 情绪类型字符串
     */
    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    /**
     * 获取鼓励信息
     * @return 鼓励建议字符串
     */
    public String getEncouragement() {
        return encouragement;
    }

    /**
     * 设置鼓励信息
     * @param encouragement 鼓励建议字符串
     */
    public void setEncouragement(String encouragement) {
        this.encouragement = encouragement;
    }

    /**
     * 获取时间戳
     * @return ISO-8601格式的时间戳字符串
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * 设置时间戳
     * @param timestamp 时间戳字符串
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}