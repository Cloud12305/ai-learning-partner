// QARequest.java
package com.digitalhorsepower.learnbuddy.dto;

import lombok.Data;

@Data
public class QARequest {
    private String question;
    private String userId;
    private String contextKp; // 追问上下文
}