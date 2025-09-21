package com.twodo0.capstoneWeb.domain;

import jakarta.persistence.*;
import lombok.*;
import org.simpleframework.xml.Element;
import java.awt.*;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ImageMeta image; // 하나의 이미지에 대해 여러 예측 가능

    private OffsetDateTime createdAt;

    private String heatmapObjectName;//MinIO에 저장된 히트맵 이미지

    // 경계상자
    @ElementCollection
    @CollectionTable(name = "prediction_detection", joinColumns = @JoinColumn(name = "prediction_id"))
    private List<Detection> detections;


    // 분류 확률 (ex. 스크래치 80%, 찌그러짐 20%..)
    @ElementCollection
    @CollectionTable(name = "prediction_classProb", joinColumns = @JoinColumn(name = "prediction_id"))
    private List<ClassProb> classProbs;

    // JPA가 컬렉션 전용 테이블을 만듬 (엔티티 테이블은 아니지만, RDB상 진짜 테이블)
    // 테이블의 각 행은 하나의 Detection 값, 여러 행이 같은 prediction_id(FK)를 가질 수 있음 (한 Prediction : 여러 Detection 구조)
    // 이 컬레션 테이블에는 PK가 강제되지 않음!
}   // "부모(prediction)에 종속되는, 개별로 관리할 필요가 없는 것들"을 관리할 떄 편함
