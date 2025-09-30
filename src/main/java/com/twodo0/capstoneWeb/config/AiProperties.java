package com.twodo0.capstoneWeb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ai")
public record AiProperties(
        String baseUrl,
        int timeoutSeconds,
        double thresholdDefault,
        Endpoints endpoints
        ) {
    public record Endpoints(String predict, String heatmap){}
}