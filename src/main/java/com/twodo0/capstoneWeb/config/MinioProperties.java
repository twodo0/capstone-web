package com.twodo0.capstoneWeb.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter @Setter
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    private String bucket; //capstone
    private long presignTtlSeconds = 3600;

    //** S3 전용 **//
    private String region; // ap-northeast-2 등

    //** MinIO 전용 **//
    private String accesskey; //minioadmin
    private String secretkey; // minioadmin
    private String endPoint; // http://localhost:9000
}
