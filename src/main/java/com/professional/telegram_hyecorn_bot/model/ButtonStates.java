package com.professional.telegram_hyecorn_bot.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ButtonStates {
    SPEND("Получить чашку кофе"),
    SUBSCRIBE("Перейти к оплате");

    private final String buttonStatusText;

    @Override
    public String toString() {
        return buttonStatusText;
    }
}
