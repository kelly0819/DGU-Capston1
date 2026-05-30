package com.capstone.backend.domain.recommendation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AgentClient {

    private final WebClient webClient;

    @Value("${ai.server-url}")
    private String aiServerUrl;

    public void runAgent(Map<String, Object> requestBody) {
        webClient.post()
                .uri(aiServerUrl + "/internal/agent/run")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe(
                        null,
                        error -> log.error("FastAPI agent 호출 실패: {}", error.getMessage())
                );
    }
}
