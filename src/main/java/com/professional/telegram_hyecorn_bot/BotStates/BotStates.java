package com.professional.telegram_hyecorn_bot.BotStates;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public enum BotStates {
    Start("start"),
    EnterPhone("enterPhone"),
    EnterEmail("enterEmail"),
    Payment("payment"),
    Approved("approved"),
    ReadyToSpend("readToSpend"),
    AllCouponsSpent("allCouponsSpent");
    private final String value;
    private final static Map<String, BotStates> STATES = new HashMap<>();

    static {
        for (BotStates s : values()) {
            STATES.put(s.value, s);
        }
    }

    public static BotStates fromValue(String value) {
        BotStates state = STATES.get(value);
        if (state == null) {
            throw new IllegalArgumentException(value);
        } else {
            return state;
        }
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static BotStates getInitialState() {
        return Start;
    }
}
