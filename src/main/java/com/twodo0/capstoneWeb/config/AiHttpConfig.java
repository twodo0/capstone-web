package com.twodo0.capstoneWeb.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(AiProperties.class)
public class AiHttpConfig {
    @Bean
    WebClient aiWebClient(WebClient.Builder builder, AiProperties props){
        return builder
                .baseUrl(props.baseUrl())
                .build();
    }
}
