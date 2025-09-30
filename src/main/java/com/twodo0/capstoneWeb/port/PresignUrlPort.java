package com.twodo0.capstoneWeb.service;

import org.springframework.stereotype.Component;

public interface PresignUrlPort {
    String presignGet(String bucket, String key, java.time.Duration ttl);
}
