package com.professional.telegram_hyecorn_bot;

import com.professional.telegram_hyecorn_bot.model.User;
import com.professional.telegram_hyecorn_bot.service.ChatBot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Contact;

@AllArgsConstructor
@Getter
public class BotContext {
    private final ChatBot bot;
    private final User user;
    private final String input;
    private final Contact contact;
}
