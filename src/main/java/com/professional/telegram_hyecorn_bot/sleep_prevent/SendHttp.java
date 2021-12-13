package com.professional.telegram_hyecorn_bot.sleep_prevent;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@EnableScheduling
@Slf4j
@Service
public class SendHttp {

    @Scheduled(fixedRateString = "1500000")
    public void job() {
        send();
    }

    @SneakyThrows
    private void send() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://hyecorn-telegram-whbot.herokuapp.com/sleepPrevent"))
                .GET()
                .build();

        log.trace("send job");
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
