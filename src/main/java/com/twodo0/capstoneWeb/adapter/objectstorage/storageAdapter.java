package com.twodo0.capstoneWeb.adapter.objectstorage;

import com.twodo0.capstoneWeb.port.ObjectStoragePort;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Component
@NoArgsConstructor
public class MinioObjectStorageAdapter implements ObjectStoragePort {

    private MinioClient minioClient;

    @Override
    public void put(String bucket, String key,  byte[] data, String contentType) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(key)
                            .contentType(contentType)
                            .stream(new ByteArrayInputStream(data), data.length, -1)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("히트맵 이미지 업로드 실패");
        }
    }


}
