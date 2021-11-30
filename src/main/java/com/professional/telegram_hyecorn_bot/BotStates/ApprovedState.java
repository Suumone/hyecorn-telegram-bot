package com.professional.telegram_hyecorn_bot.BotStates;

import com.professional.telegram_hyecorn_bot.BotContext;
import com.professional.telegram_hyecorn_bot.ButtonStates;

public class ApprovedState extends StateAbstract {
    @Override
    public void enter(BotContext context) {
        sendMessageWithButton(context,
                "Поздравляем, вам доступно " + context.getUser().getCouponsNumber() + " чашек кофе, можете тратить их в любое время в любом количестве! Чтобы получить первую чашку кофе - при бариста нажмите на кнопку \"Получить чашку кофе\" ниже.",
                ButtonStates.SPEND);
    }

    @Override
    public BotStates nextState(BotContext context) {
        return BotStates.ReadyToSpend;
    }

    @Override
    public BotStates currentState() {
        return BotStates.Approved;
    }
}
