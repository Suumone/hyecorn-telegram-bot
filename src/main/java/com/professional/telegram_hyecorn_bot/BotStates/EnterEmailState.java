package com.professional.telegram_hyecorn_bot.BotStates;

import com.professional.telegram_hyecorn_bot.BotContext;
import com.professional.telegram_hyecorn_bot.utils.Utils;

public class EnterEmailState extends StateAbstract {
    private BotStates next;

    @Override
    public void enter(BotContext context) {
        sendMessageWithKeyboardRemove(context, "Введите почту");
    }

    @Override
    public void handleInput(BotContext context) {
        String email = context.getInput();

        if (Utils.isValidEmail(email)) {
            context.getUser().setEmail(email);
            next = BotStates.Payment;
        } else {
            sendMessage(context, "Некорректная почта");
            next = BotStates.EnterEmail;
        }
    }

    @Override
    public BotStates nextState(BotContext context) {
        return next;
    }

    @Override
    public BotStates currentState() {
        return BotStates.EnterEmail;
    }
}
