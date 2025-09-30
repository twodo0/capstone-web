package com.twodo0.capstoneWeb.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "prediction")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prediction extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ImageMeta image; // 하나의 이미지에 대해 여러 예측 가능

    // private OffsetDateTime createdAt;

    private String heatmapBucket; // MinIO에 저장될 히트맵의 bucket
    private String heatmapKey;//MinIO에 저장된 히트맵 이미지 이름

    // 경계상자
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "prediction", cascade = CascadeType.ALL)
    private List<Detection> detections;


    // 연관관계 편의 메서드
    public void addDetection(Detection detection){
        detections.add(detection);
        detection.setPrediction(this);
    }

    public void deleteDetection(Detection detection){
        detections.remove(detection);
        detection.setPrediction(null);
    }
}
