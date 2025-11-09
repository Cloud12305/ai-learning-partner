package com.digitalhorsepower.learnbuddy.dto;

public class PsychologyRequest {
    private String text;
    private String userId;

    // 构造器、getter、setter
    public PsychologyRequest() {}

    public PsychologyRequest(String text, String userId) {
        this.text = text;
        this.userId = userId;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}