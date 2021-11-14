package com.professional.telegram_hyecorn_bot;

import com.professional.telegram_hyecorn_bot.model.User;
import com.professional.telegram_hyecorn_bot.utils.Utils;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.professional.telegram_hyecorn_bot.ButtonStates.SPEND;

public enum BotState {
    Start {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Приветствую, " + context.getUser().getFirstName() + "!");
            sendMessage(context, "описание подписки и сервиса");
        }

        @Override
        public BotState nextState(BotContext context) {
            return EnterPhone;
        }
    },

    EnterPhone {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Введите номер телефона:");
        }

        @Override
        public void handleInput(BotContext context) {
            context.getUser().setPhone(context.getInput());
        }

        @Override
        public BotState nextState(BotContext context) {
            return EnterEmail;
        }
    },

    EnterEmail {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Введите почту:");
        }

        @Override
        public void handleInput(BotContext context) {
            String email = context.getInput();

            if (Utils.isValidEmail(email)) {
                context.getUser().setEmail(context.getInput());
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
                    "Поздравляем, вам доступно " + context.getUser().getCouponsNumber() + " купонов, можете тратить их в любое время в любом количестве!",
                    SPEND);
        }

        @Override
        public BotState nextState(BotContext context) {
            return ReadyToSpend;
        }
    },

    ReadyToSpend {

        @Override
        public void enter(BotContext context) {
            sendMessage(context, "наверно вывод чтото типо штрих кода или qr-кода");
            sendMessage(context, "вакцинации");
            sendMessageWithPicture(context, "https://pbs.twimg.com/media/Ep7JUEmW8AAuolJ?format=jpg&name=large");

            sendMessage(context, "потратился 1 купон");
            User user = context.getUser();
            user.setCouponsNumber(user.getCouponsNumber() - 1);

            if (user.getCouponsNumber() > 0) {
                sendMessageWithButton(context,
                        "Супер, вы потратили купон, вам доступно еще " + user.getCouponsNumber() + " купонов в рамках подписки",
                        SPEND);
            } else {
                sendMessage(context, "Спасибо, что пользуетесь нашим сервисом! У вас не осталось купонов.");
                sendMessageWithButton(context,
                        "Хотите оформить повторно для этого же ресторана?",
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
                button.setText("Потратить купон");
                button.setCallbackData("SPEND");
                break;
            case SUBSCRIBE:
                button.setText("Подписаться");
                button.setCallbackData("SUBSCRIBE");
                break;
        }

        rows.get(0).add(button);
        inlineKeyboardMarkup.setKeyboard(rows);

        SendMessage message = SendMessage.builder()
                .chatId(Long.toString(context.getUser().getChatId()))
                .text(text)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
        context.getBot().execute(message);
    }

    @SneakyThrows
    protected void sendMessageWithPicture(BotContext context, String url) {

        SendPhoto message = SendPhoto.builder()
                .chatId(Long.toString(context.getUser().getChatId()))
                .photo(new InputFile(url))
                .build();
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
