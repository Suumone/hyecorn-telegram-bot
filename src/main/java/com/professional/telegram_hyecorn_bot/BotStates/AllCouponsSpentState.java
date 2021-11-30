package com.professional.telegram_hyecorn_bot.BotStates;

import com.professional.telegram_hyecorn_bot.BotContext;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AllCouponsSpentState extends StateAbstract {
    @Override
    public void enter(BotContext context) {
    }

    @Override
    public BotStates nextState(BotContext context) {
        return BotStates.Payment;
    }

    @Override
    public boolean isEnterImmediately() {
        return true;
    }

    @Override
    public BotStates currentState() {
        return BotStates.AllCouponsSpent;
    }
}
