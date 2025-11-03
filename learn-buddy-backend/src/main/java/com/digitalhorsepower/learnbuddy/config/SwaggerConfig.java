package com.digitalhorsepower.learnbuddy.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI learnBuddyOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AI 智能·学习搭子系统 API")
                        .description("学习搭子系统后端API文档")
                        .version("v1.0"));
    }
}