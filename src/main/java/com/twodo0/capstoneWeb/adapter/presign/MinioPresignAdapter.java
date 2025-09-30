package com.twodo0.capstoneWeb.adapter;

import com.twodo0.capstoneWeb.config.MinioProperties;
import com.twodo0.capstoneWeb.port.PresignUrlPort;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("minio")
@RequiredArgsConstructor
public class MinioPresignAdapter implements PresignUrlPort {

    private final MinioClient minio; // Config의 bean이 주입됨
    private final MinioProperties props; // yml 바인딩 값 주입


    @Override
    public String presignGet(String bucket, String key, java.time.Duration ttl) {
        try {
            if (bucket == null || bucket.isBlank()) {
                throw new IllegalArgumentException("bucket is null or blank");
            }
            if (key == null || key.isBlank()) {
                throw new IllegalArgumentException("key is null or blank");
            }

            int seconds = (int) Math.min(ttl.toSeconds(), props.getPresignTtlSeconds());
            return minio.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET) // GET 만 허용
                            .bucket(bucket) // 대상 버킷
                            .object(key) // 대상 오프젝트 키
                            .expiry(seconds) // 유효시간(초)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("MinIO presign failed for key = " + key, e);
        }
    }

}
