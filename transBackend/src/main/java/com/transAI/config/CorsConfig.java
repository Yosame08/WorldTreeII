package com.transAI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
    // 添加映射路径，“/”表示所有的路径都会被映射到该配置下。
        registry.addMapping("/**")  
                //是否发送Cookie
                .allowCredentials(true)
                //放行哪些原始域
                .allowedOriginPatterns("*")
                // 允许的请求方法，包括GET、POST、PUT和DELETE。
                .allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"})
                // 允许的请求头信息，即任何请求头都允许。
                .allowedHeaders("*")
                // 允许的响应头信息，即任何响应头都允许。
                .exposedHeaders("*");
    }
}

