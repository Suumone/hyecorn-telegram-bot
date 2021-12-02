package com.professional.telegram_hyecorn_bot.BotStates;

import com.professional.telegram_hyecorn_bot.BotContext;

public class WaitingForPaymentState extends StateAbstract {
    @Override
    public void enter(BotContext context) {}

    @Override
    public BotStates nextState(BotContext context) {
        return BotStates.Approved;
    }

    @Override
    public BotStates currentState() {
        return BotStates.WaitingForPayment;
    }
}
