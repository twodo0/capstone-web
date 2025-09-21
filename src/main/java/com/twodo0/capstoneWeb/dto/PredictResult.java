package com.twodo0.capstoneWeb.dto;

import lombok.*;

import java.util.List;

// !!-- FastAPI 응답 DTO --!!

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PredictResult {
    private String model;
    private String heatmapBase64;// base64 PNG
    private List<ClassProb> classes; // 이미지 레벨 분류 확률
    private List<Box> detections; // 객체탑지

    private class ClassProb {
        private String label;
        private double prob;
    }
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    private class Box {
        private String label; // label -> 추후 enum으로 변경
        private double prob; // 확률 -> 나중에 json으로 여러 label에 대해서 확률 포함할 수도..?
        private int x, y, w, h; // 경계상자 좌표
    }
}

