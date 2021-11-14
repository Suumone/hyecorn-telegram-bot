package com.professional.telegram_hyecorn_bot;

import com.professional.telegram_hyecorn_bot.model.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BotContext {
    private final ChatBot bot;
    private final User user;
    private final String input;

    public ChatBot getBot() {
        return bot;
    }

    public User getUser() {
        return user;
    }

    public String getInput() {
        return input;
    }
}
