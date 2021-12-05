package com.professional.telegram_hyecorn_bot.BotStates;

import com.professional.telegram_hyecorn_bot.BotContext;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PaymentState extends StateAbstract {

    @Override
    public void enter(BotContext context) {
        sendKeyboardRemove(context, "Здесь будет оплата");

        context.getUser().setCouponsNumber(context.getUser().getCouponsNumber() + 10);
    }

    @Override
    public BotStates nextState(BotContext context) {
        return BotStates.Approved;
    }

    @Override
    public boolean isEnterImmediately() {
        return true;
    }

    @Override
    public BotStates currentState() {
        return BotStates.Payment;
    }
}
