package com.agentic.cicd.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class AlertService {

    public void send(String message) {
        String token = "YOUR_TELEGRAM_BOT_TOKEN";
        String chatId = "YOUR_CHAT_ID";
        String url = String.format(
                "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
                token, chatId, URLEncoder.encode(message, StandardCharsets.UTF_8)
        );

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForEntity(URI.create(url), String.class);
        System.out.println("📣 Telegram alert sent: " + message);
    }
}
