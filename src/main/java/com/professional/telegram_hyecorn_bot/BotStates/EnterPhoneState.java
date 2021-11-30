package com.professional.telegram_hyecorn_bot.BotStates;

import com.professional.telegram_hyecorn_bot.BotContext;
import com.professional.telegram_hyecorn_bot.utils.Utils;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EnterPhoneState extends StateAbstract {
    private BotStates next;

    @Override
    public void enter(BotContext context) {
        //кнопка убирается в начале следующего стейта(EnterEmailState.enter)
        sendMessageRequestPhone(context, "Введите номер телефона или поделитесь, нажав кнопку ниже ⏬");
    }

    @Override
    public void handleInput(BotContext context) {
        if (context.getContact() != null && context.getContact().getPhoneNumber() != null) {
            context.getUser().setPhone(Utils.validatePhoneNumber(context.getContact().getPhoneNumber()));
            next = BotStates.EnterEmail;
            return;
        }

        String phoneNumber = context.getInput();
        if (Utils.isValidPhoneNumber(phoneNumber)) {
            context.getUser().setPhone(Utils.validatePhoneNumber(phoneNumber));
            next = BotStates.EnterEmail;
        } else {
            sendMessage(context, "Некорректный номер");
            next = BotStates.EnterPhone;
        }
    }

    @Override
    public BotStates nextState(BotContext context) {
        return next;
    }

    @Override
    public BotStates currentState() {
        return BotStates.EnterPhone;
    }
}
