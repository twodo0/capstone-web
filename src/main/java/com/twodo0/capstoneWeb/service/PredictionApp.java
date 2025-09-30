package com.twodo0.capstoneWeb.service;

import com.twodo0.capstoneWeb.config.AiProperties;
import com.twodo0.capstoneWeb.config.MinioProperties;
import com.twodo0.capstoneWeb.domain.ImageMeta;
import com.twodo0.capstoneWeb.domain.Prediction;
import com.twodo0.capstoneWeb.dto.FastApiPredictRes;
import com.twodo0.capstoneWeb.port.InferencePort;
import com.twodo0.capstoneWeb.port.ObjectStoragePort;
import com.twodo0.capstoneWeb.port.PresignUrlPort;
import com.twodo0.capstoneWeb.repository.ImageRepository;
import com.twodo0.capstoneWeb.repository.PredictionRepository;
import com.twodo0.capstoneWeb.service.mapper.FastApiMapper;
import com.twodo0.capstoneWeb.storage.StorageKeyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PredictionApp { // 실제로 사진을 저장하고, FastAPI를 호출하는 서비스
    private final MinioProperties minioProperties;
    private final ImageRepository imageRepository;
    private final PredictionRepository predictionRepository;
    private final ObjectStoragePort storageAdapter;
    private final  PresignUrlPort presignUrlPort;
    private final InferencePort fastApiAdapter;
    private final StorageKeyFactory keyFactory;

    public Long runAndSave(Long imageId, Double threshold, String model){
        ImageMeta imageMeta = imageRepository.findById(imageId).orElseThrow();
        String rawUrl = presignUrlPort.presignGet(imageMeta.getBucket(), imageMeta.getKey());

        //추론 결과 + heatmapId 반환
        FastApiPredictRes res = fastApiAdapter.predictByUrl(rawUrl, threshold, model);

        // controller에 던져줄 Prediction 객체 생성
        Prediction p = Prediction.builder()
                .image(imageMeta)
                .detections(FastApiMapper.toDetections(res.boxes()))
                .build();

        // 일단 영속화
        p = predictionRepository.save(p);

        if(res.heatmapId() != null){
            try {
                // FastApi로부터 heatmap을 byte 로 받음
                byte[] bytes = fastApiAdapter.downloadHeatmap(res.heatmapId());

                // MinIO에 저장
                String heatmapBucket = minioProperties.getHeatmapBucket();
                String heatmapKey = keyFactory.getHeatmapKey(p.getId());

                storageAdapter.put(heatmapBucket, heatmapKey, bytes, "image/png");
                p.setHeatmapBucket(heatmapBucket);
                p.setHeatmapKey(heatmapKey);
            } catch (Exception e){
                throw new RuntimeException("Heatmap upload failed", e);
            }
        }
        // 비동기 처리를 위해 predictionId만 리턴
        return p.getId();
    }

}
