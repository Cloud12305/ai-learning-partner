package com.digitalhorsepower.learnbuddy.dto;

public class PsychologyResponse {
    private String emotion;
    private String encouragement;
    private String timestamp;

    // 构造器、getter、setter
    public PsychologyResponse() {}

    public PsychologyResponse(String emotion, String encouragement) {
        this.emotion = emotion;
        this.encouragement = encouragement;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    // getter 和 setter 方法
    public String getEmotion() { return emotion; }
    public void setEmotion(String emotion) { this.emotion = emotion; }

    public String getEncouragement() { return encouragement; }
    public void setEncouragement(String encouragement) { this.encouragement = encouragement; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}