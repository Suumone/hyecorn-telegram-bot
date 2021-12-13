package com.professional.telegram_hyecorn_bot;

import com.professional.telegram_hyecorn_bot.service.ChatBot;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@AllArgsConstructor
public class WebHookController {

    private ChatBot chatBot;

    @SneakyThrows
    @PostMapping(value = "/")
    public void onWebhookUpdateReceived(@RequestBody Update update) {

        chatBot.onUpdateReceived(update);
    }
}
