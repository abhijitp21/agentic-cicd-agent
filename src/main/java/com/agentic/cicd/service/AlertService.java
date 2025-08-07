package com.agentic.cicd.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class AlertService {

    private final WebClient webClient;

    public AlertService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.telegram.org").build();
    }

    public void send(String message) {
        String token = "YOUR_TELEGRAM_BOT_TOKEN";
        String chatId = "YOUR_CHAT_ID";
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);

        String url = String.format("/bot%s/sendMessage?chat_id=%s&text=%s", token, chatId, encodedMessage);

        webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> System.out.println("📣 Telegram alert sent: " + message))
                .block(); // Block for simplicity
    }
}