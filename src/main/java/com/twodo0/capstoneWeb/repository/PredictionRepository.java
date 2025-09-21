package com.twodo0.capstoneWeb.repository;

import com.twodo0.capstoneWeb.domain.Prediction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

public interface PredictionRepository {
    @EntityGraph(attributePaths = {"image", "classProbs", "detections"})
    Page<Prediction> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
