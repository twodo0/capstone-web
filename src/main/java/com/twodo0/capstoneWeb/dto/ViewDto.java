package com.twodo0.capstoneWeb.dto;

import com.twodo0.capstoneWeb.domain.ClassProb;
import com.twodo0.capstoneWeb.domain.Detection;
import com.twodo0.capstoneWeb.domain.ImageMeta;
import com.twodo0.capstoneWeb.domain.enums.DamageType;

import java.util.List;

public record ViewDto(
        DamageType damageType,
        ImageMeta imageMeta,
        String heatmapObjectName,
        List<Detection> detections,
        List<ClassProb> classProbs
) {
}
