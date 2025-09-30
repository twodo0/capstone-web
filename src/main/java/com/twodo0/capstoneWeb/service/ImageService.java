package com.twodo0.capstoneWeb.service;

import com.twodo0.capstoneWeb.config.MinioProperties;
import com.twodo0.capstoneWeb.domain.ImageMeta;
import com.twodo0.capstoneWeb.port.ObjectStoragePort;
import com.twodo0.capstoneWeb.repository.ImageRepository;
import com.twodo0.capstoneWeb.storage.StorageKeyFactory;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final MinioClient minioClient;
    private final MinioProperties props;
    private final StorageKeyFactory storageKeyFactory;
    private final ObjectStoragePort storageAdapter;

    @Transactional
    public ImageMeta upload(MultipartFile file) {
        try {
            // 1. 파일 바이트/메타 읽기 (한 번 읽어두고 재사용)
            byte[] bytes = file.getBytes();

            //content-type 신뢰성 보강 (가능하면 스니핑???)
            String ct = file.getContentType();
            if(ct == null || ct.isBlank()) {
                ct = "application/octet-stream";
            }

            // 이미지 크기 확인용으로 ByteArrayInputStream으로 (없으면 null 허용)
            var bi = ImageIO.read(new ByteArrayInputStream(bytes));
            Integer width = (bi!=null) ? bi.getWidth() : null;
            Integer height = (bi!=null) ? bi.getHeight() : null;

            // 2. 오브젝트 키 생성 : raw/yyyy/MM/uuid.ext
            String key = storageKeyFactory.getRawkey(file, ct);

            String bucket = props.getRawBucket();
            ensureBucket(bucket);

            //3. MinIO 업로드
            storageAdapter.put(bucket, key, bytes, ct);

            // 4. DB 저장
            ImageMeta imageMeta = ImageMeta.builder()
                    .bucket(bucket)
                    .key(key)
                    .width(width)
                    .height(height)
                    .build();

            return imageRepository.save(imageMeta);

        } catch (Exception e) {
            throw new RuntimeException("이미지 업로드 실패", e);
        }
    }


    // --helpers ---
    private void ensureBucket(String bucket) throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }
}
