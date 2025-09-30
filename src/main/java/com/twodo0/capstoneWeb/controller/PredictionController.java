package com.twodo0.capstoneWeb.controller;

import com.twodo0.capstoneWeb.dto.PredictionDetailDto;
import com.twodo0.capstoneWeb.dto.PredictionRowView;
import com.twodo0.capstoneWeb.service.PredictionApp;
import com.twodo0.capstoneWeb.service.PredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/predictions")
@RequiredArgsConstructor
public class PredictionController {

    private final PredictionApp app;
    private final PredictionService predictionService;

    // 읽기, 쓰기 별도 호출 !!

    // 예측 생성 + 단건 상세 조회(동기)
    @PostMapping("/by-image/{imageId}")
    public ResponseEntity<PredictionDetailDto> predictionById(
            @PathVariable Long imageId,
            @RequestParam(required = false) Double threshold,
            @RequestParam(required = false) String model)
    {
        // 저장 및 조회
        Long id = app.runAndSave(imageId, threshold, model);

        // 프론트에 던져줄 객체
        PredictionDetailDto detail = predictionService.getPredictionDetail(id);

        // Location 헤더에 들어갈 방금 생성된 리소스의 주소
        // 201 Created + Location으로 나중에 그 URL로 GET을 보내 상세 정보를 다시 가져올 수 있음
        URI location  = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/predictions/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).body(detail);
    }

    // 단건 상세 조회
    @GetMapping("/{predictionId}")
    public PredictionDetailDto getOne(@PathVariable Long predictionId) {
        return predictionService.getPredictionDetail(predictionId);
    }

    //목록 조회
    @GetMapping("/recent")
    public Page<PredictionRowView> recentPredictions(
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        return predictionService.findRecent(pageable);
    }

}
