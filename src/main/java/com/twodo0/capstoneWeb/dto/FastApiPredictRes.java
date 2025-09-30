package com.twodo0.capstoneWeb.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.twodo0.capstoneWeb.domain.enums.DamageType;

import java.util.List;

// FastAPI가 PredictionApp에 내려주는 응답 DTO
@JsonIgnoreProperties(ignoreUnknown = true)
public record FastApiPredictRes(
        String model,
        ImageInfo imageInfo, // 프로튼에서 원본 위에 박스를 그릴 때 기준이 필요함.
        @JsonProperty("threshold_used") Double thresholdUsed,

        @JsonProperty("boxes")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        List<BoxDto> boxes,

        @JsonProperty("heatmap_id")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String heatmapId
){

    public record BoxDto(
            @JsonProperty("label")
            DamageType damageType,

            double prob,
            int x, int y, int w, int h
    ) {};
    public record ImageInfo(
            int width,
            int height
    ){};
}

