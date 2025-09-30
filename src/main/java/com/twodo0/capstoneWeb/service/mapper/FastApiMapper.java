package com.twodo0.capstoneWeb.service.mapper;


import com.twodo0.capstoneWeb.domain.ClassProb;
import com.twodo0.capstoneWeb.domain.Detection;
import com.twodo0.capstoneWeb.dto.FastApiPredictRes;

import java.util.List;

public class FastApiMapper {
    public static List<ClassProb> toClassProbs(List<FastApiPredictRes.ClassProbDto> src) {
        if(src == null || src.isEmpty()) return List.of();
        return src.stream().map(c -> ClassProb.builder()
                .prob(c.prob())
                .damageType(c.damageType())
                .build())
                .toList();
    }

    public static List<Detection> toDetections(List<FastApiPredictRes.BoxDto> src) {
        if(src == null || src.isEmpty()) return List.of();
        return src.stream().map(d -> Detection.builder()
                .damageType(d.damageType())
                .prob(d.prob())
                .x(d.x()).y(d.y()).w(d.w()).h(d.h())
                .build())
                .toList();
    }

}
