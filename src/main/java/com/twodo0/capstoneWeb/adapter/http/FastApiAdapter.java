package com.twodo0.capstoneWeb.adapter.http;

import com.twodo0.capstoneWeb.config.AiProperties;
import com.twodo0.capstoneWeb.dto.FastApiPredictRes;
import com.twodo0.capstoneWeb.port.InferencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class FastApiAdapter implements InferencePort {

    private final WebClient webClient;
    private final AiProperties props;

    @Override
    public FastApiPredictRes predictByUrl(String imageUrl, double threshold, String model) {
        var req = Map.of(
                "image_url", imageUrl,
                "threshold", threshold,
                "model", model);

        //FastAPI한테 이 imageUrl 들고 가서 추론해달라고 POST 요청
        // 추론에 대한 응답을 FastApiPredictsRes로 역직렬화
        return webClient.post()
                .uri(props.endpoints().predict()) //WebClient 빈을 만들 때 baseUrl까지 넣어줌
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .retrieve() // 응답 꺼낼 준비
                .onStatus(HttpStatusCode::isError, r->
                        r.bodyToMono(String.class).defaultIfEmpty("")
                                .map(msg -> new RuntimeException("FastAPI / predict error: " + r.statusCode() + ": " + msg))
                        )
                .bodyToMono(FastApiPredictRes.class)
                .doOnError(e -> log.error("Decode faild", e))
                .block(Duration.ofSeconds(props.timeoutSeconds())); // 동기 대기(타임아웃 포함)
    }

    @Override
    public byte[] downloadHeatmap(String heatmapId) {
        return webClient.get()
                .uri(b -> b.path(props.endpoints().heatmap()).build(Map.of("heatmapId", heatmapId)))
                .accept(MediaType.IMAGE_PNG)
                .retrieve()
                .onStatus(HttpStatusCode::isError, r ->
                        r.bodyToMono(String.class).defaultIfEmpty("")
                        .map(msg -> new RuntimeException("FastAPI /heatmaps error: " + r.statusCode() + ": " + msg))
                ).bodyToMono(byte[].class)
                .block(Duration.ofSeconds(props.timeoutSeconds()));
    }
}
