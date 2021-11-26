package com.professional.telegram_hyecorn_bot;

import com.professional.telegram_hyecorn_bot.model.User;
import com.professional.telegram_hyecorn_bot.utils.Utils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public enum BotState {
    Start {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Приветствую, " + context.getUser().getFirstName() + "!");
            sendMessage(context, "Это бот для оформления подписки на любимый кофе. Как это работает: Вы покупаете 10 чашек любимого кофе по цене 8! Вы оплачиваете сумму за 8 чашек кофе единоразовым платежом для оформления подписки. По подписке Вам становится доступно 10 чашек кофе, Вы можете тратить их в этой кофейне в любое время в любом количестве. Обратите внимание, что все эти чашки кофе надо выпить в течение месяца. Те чашки кофе, которые Вы не успеете выпить в течение месяца, сгорят :( Подписку нельзя использовать в других кофейнях. После оплаты, чтобы получить чашку кофе - при бариста нажмите на кнопку \"Получить чашку кофе\" ниже, тогда вам будет доступно уже не 10, а 9 чашек. Ссылка на оплату - в сообщении ниже. Вкусного Вам кофе! :)");
        }

        @Override
        public BotState nextState(BotContext context) {
            return EnterPhone;
        }
    },

    EnterPhone {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Введите номер телефона");
        }

        @Override
        public void handleInput(BotContext context) {

            String phoneNumber = context.getInput();
            if (Utils.isValidPhoneNumber(phoneNumber)) {
                context.getUser().setPhone(Utils.validatePhoneNumber(phoneNumber));
                next = EnterEmail;
            } else {
                sendMessage(context, "Некорректный телефон");
                next = EnterPhone;
            }
        }

        @Override
        public BotState nextState(BotContext context) {
            return next;
        }
    },

    EnterEmail {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Введите почту");
        }

        @Override
        public void handleInput(BotContext context) {
            String email = context.getInput();

            if (Utils.isValidEmail(email)) {
                context.getUser().setEmail(email);
                next = Payment;
            } else {
                sendMessage(context, "Некорректная почта");
                next = EnterEmail;
            }
        }

        @Override
        public BotState nextState(BotContext context) {
            return next;
        }
    },

    Payment(true) {
        @Override
        public void enter(BotContext context) {
            User user = context.getUser();
            user.setCouponsNumber(user.getCouponsNumber() + 10);
            sendMessage(context, "Здесь будет оплата");
        }

        @Override
        public void handleInput(BotContext context) {
        }

        @Override
        public BotState nextState(BotContext context) {
            return Approved;
        }
    },

    Approved {
        @Override
        public void enter(BotContext context) {
            sendMessageWithButton(context,
                    "Поздравляем, вам доступно " + context.getUser().getCouponsNumber() + " чашек кофе, можете тратить их в любое время в любом количестве! Чтобы получить первую чашку кофе - при бариста нажмите на кнопку \"Получить чашку кофе\" ниже.",
                    ButtonStates.SPEND);
        }

        @Override
        public BotState nextState(BotContext context) {
            return ReadyToSpend;
        }
    },

    ReadyToSpend {
        @Override
        public void enter(BotContext context) {
            context.getUser().setCouponsNumber(context.getUser().getCouponsNumber() - 1);

            if (context.getUser().getCouponsNumber() > 0) {
                sendMessageWithButton(context,
                        "Приятного Вам кофе! В этом месяце Вам доступно еще " + context.getUser().getCouponsNumber() + " чашек :)",
                        ButtonStates.SPEND);
            } else {
                sendMessage(context, "Спасибо, что пользуетесь нашим сервисом! У вас не осталось чашек кофе по подписке.");
                sendMessageWithButton(context,
                        "Хотите оформить подписку повторно?",
                        ButtonStates.SUBSCRIBE);
            }
        }

        @Override
        public BotState nextState(BotContext context) {
            return context.getUser().getCouponsNumber() > 0 ? ReadyToSpend : AllCouponsSpent;
        }
    },

    AllCouponsSpent(true) {
        @Override
        public void enter(BotContext context) {
        }

        @Override
        public BotState nextState(BotContext context) {
            return Payment;
        }
    };

    @SneakyThrows
    protected void sendMessage(BotContext context, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(Long.toString(context.getUser().getChatId()))
                .text(text)
                .build();

        log.trace("Sending message:{}", message.toString());
        context.getBot().execute(message);
    }

    @SneakyThrows
    protected void sendMessageWithButton(BotContext context, String text, ButtonStates buttonState) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(new ArrayList<>());

        InlineKeyboardButton button = new InlineKeyboardButton();
        switch (buttonState) {
            case SPEND:
                button.setText(ButtonStates.SPEND.toString());
                button.setCallbackData(ButtonStates.SPEND.name());
                //button.setUrl("google.com");
                break;
            case SUBSCRIBE:
                button.setText(ButtonStates.SUBSCRIBE.toString());
                button.setCallbackData(ButtonStates.SUBSCRIBE.name());
                break;
        }

        rows.get(0).add(button);
        inlineKeyboardMarkup.setKeyboard(rows);

        SendMessage message = SendMessage.builder()
                .chatId(Long.toString(context.getUser().getChatId()))
                .text(text)
                .replyMarkup(inlineKeyboardMarkup)
                .build();

        log.trace("Sending message with buttons:{}", message.toString());
        context.getBot().execute(message);
    }

    @SneakyThrows
    protected void sendMessageWithPicture(BotContext context, String url) {

        SendPhoto message = SendPhoto.builder()
                .chatId(Long.toString(context.getUser().getChatId()))
                .photo(new InputFile(url))
                .build();

        log.trace("Sending photo:{}", message.toString());
        context.getBot().execute(message);
    }

    private static BotState[] states;

    private final boolean enterImmediately;

    BotState() {
        this.enterImmediately = false;
    }

    BotState(boolean enterImmediately) {
        this.enterImmediately = enterImmediately;
    }

    public static BotState byId(int id) {
        if (states == null) {
            states = BotState.values();
        }

        return states[id];
    }

    public static BotState getInitialState() {
        return byId(0);
    }

    public abstract void enter(BotContext context);

    public void handleInput(BotContext context) {
    }

    public abstract BotState nextState(BotContext context);

    public boolean isEnterImmediately() {
        return enterImmediately;
    }
}
