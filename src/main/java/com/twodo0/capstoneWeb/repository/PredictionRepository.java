package com.twodo0.capstoneWeb.repository;

import com.twodo0.capstoneWeb.domain.Prediction;
import com.twodo0.capstoneWeb.dto.PredictionRowDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PredictionRepository extends JpaRepository<Prediction, Long> {

    // DTO 프로젝션으로 한방에 쿼리를 날리기 떄문에 N+1 안 생김
    @Query(value = """
    select new com.twodo0.capstoneWeb.dto.PredictionRowDto(
    p.id, p.createdAt, i.bucket, i.key, count(d.id)
    )
    from Prediction p join p.image i
    left join p.detections d
    group by p.id, p.createdAt, i.bucket, i.key
""",
    countQuery = "select count(p) from Prediction p")
    Page<PredictionRowDto> findRecentRows(Pageable pageable);

    @EntityGraph(attributePaths = {"image", "classProbs", "detections"})
    @Query("select p from Prediction p where p.id = :id")
    Prediction findWithAllById(long id);
}
