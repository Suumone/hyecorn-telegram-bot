package com.professional.telegram_hyecorn_bot.BotStates;

import com.professional.telegram_hyecorn_bot.BotContext;
import com.professional.telegram_hyecorn_bot.model.ButtonStates;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ReadyToSpendState extends StateAbstract {

    @Override
    public void enter(BotContext context) {
        context.getUser().setCouponsNumber(context.getUser().getCouponsNumber() - 1);

        if (context.getUser().getCouponsNumber() > 0) {
            sendMessageWithButton(context,
                    "Приятного Вам кофе! В этом месяце Вам доступно еще " + context.getUser().getCouponsNumber() + " чашек :)",
                    ButtonStates.SPEND);
        } else {
            sendMessage(context, "Спасибо, что пользуетесь нашим сервисом! У вас не осталось чашек кофе по подписке.");
            sendMessage(context, "Хотите оформить подписку повторно?");
            sendPaymentMessage(context);
        }
    }

    @Override
    public BotStates nextState(BotContext context) {
        return context.getUser().getCouponsNumber() > 0 ? BotStates.ReadyToSpend : BotStates.AllCouponsSpent;
    }

    @Override
    public BotStates currentState() {
        return BotStates.ReadyToSpend;
    }
}
