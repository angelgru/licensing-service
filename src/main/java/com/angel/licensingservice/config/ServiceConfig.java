package com.angel.licensingservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceConfig {
    @Value("${example.property}")
    private String exampleProperty;

    @Value("${redis.server}")
    private String redisServer;

    @Value("${redis.port}")
    private String redisPort;

    public String getExampleProperty() {
        return exampleProperty;
    }

    public String getRedisServer() {
        return redisServer;
    }

    public String getRedisPort() {
        return redisPort;
    }
}
