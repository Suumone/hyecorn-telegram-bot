package com.professional.telegram_hyecorn_bot.BotStates;

import com.professional.telegram_hyecorn_bot.BotContext;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StartState extends StateAbstract {

    @Override
    public void enter(BotContext context) {
        sendMessage(context, "Приветствую, " + context.getUser().getFirstName() + "!");
        sendMessage(context, "Это бот для оформления подписки на любимый кофе. Как это работает: Вы покупаете 10 чашек любимого кофе по цене 8! Вы оплачиваете сумму за 8 чашек кофе единоразовым платежом для оформления подписки. По подписке Вам становится доступно 10 чашек кофе, Вы можете тратить их в этой кофейне в любое время в любом количестве. Обратите внимание, что все эти чашки кофе надо выпить в течение месяца. Те чашки кофе, которые Вы не успеете выпить в течение месяца, сгорят :( Подписку нельзя использовать в других кофейнях. После оплаты, чтобы получить чашку кофе - при бариста нажмите на кнопку \"Получить чашку кофе\" ниже, тогда вам будет доступно уже не 10, а 9 чашек. Ссылка на оплату - в сообщении ниже. Вкусного Вам кофе! :)");
    }

    @Override
    public BotStates nextState(BotContext context) {
        return BotStates.EnterPhone;
    }

    @Override
    public BotStates currentState() {
        return BotStates.Start;
    }
}
