package com.twodo0.capstoneWeb.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "detection")
public class Detection {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prediction_id", nullable = false)
    private Prediction prediction;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "detection_per_probs", joinColumns = @JoinColumn(name = "detection_id")) // 값 타입 테이블에 생길 FK이름(부모 id와 같을 필요 X)
    @OrderBy("prob DESC ") // JPA로 조회할 때 정렬된 상태로 조회
    private List<ClassProb> classProbs;

    private int x; private int y; //좌상단 (원본 픽셀 기준)
    private int w; private int h; //너비 높이
}