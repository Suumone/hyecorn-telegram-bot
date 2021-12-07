package com.professional.telegram_hyecorn_bot;

import com.professional.telegram_hyecorn_bot.service.ChatBot;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@AllArgsConstructor
public class WebHookController {

    private ChatBot chatBot;

    @SneakyThrows
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void onWebhookUpdateReceived(@RequestBody Update update) {

        chatBot.onUpdateReceived(update);
    }
}
