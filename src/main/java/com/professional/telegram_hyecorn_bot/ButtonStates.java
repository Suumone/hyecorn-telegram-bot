package com.professional.telegram_hyecorn_bot;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ButtonStates {
    SPEND("Потратить купон"),
    SUBSCRIBE("Подписаться");

    private final String buttonStatusText;

    @Override
    public String toString() {
        return buttonStatusText;
    }
}
