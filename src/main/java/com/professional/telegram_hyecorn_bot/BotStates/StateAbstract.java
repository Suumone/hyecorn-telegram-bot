package com.professional.telegram_hyecorn_bot.BotStates;

import com.professional.telegram_hyecorn_bot.BotContext;
import com.professional.telegram_hyecorn_bot.ButtonStates;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;

@Slf4j
public abstract class StateAbstract {

    interface ObjFactory {
        StateAbstract create();
    }
    private static final Map<BotStates, ObjFactory> factoryMap;

    static {
        final HashMap<BotStates, ObjFactory> map = new HashMap<>();
        map.put(BotStates.Start, StartState::new);
        map.put(BotStates.EnterPhone, EnterPhoneState::new);
        map.put(BotStates.EnterEmail, EnterEmailState::new);
        map.put(BotStates.Payment, PaymentState::new);
        map.put(BotStates.Approved, ApprovedState::new);
        map.put(BotStates.ReadyToSpend, ReadyToSpendState::new);
        map.put(BotStates.AllCouponsSpent, AllCouponsSpentState::new);

        factoryMap = Collections.unmodifiableMap(map);
    }

    public static StateAbstract create(BotStates state) {
        ObjFactory objFactory = factoryMap.get(state);
        Objects.requireNonNull(objFactory, "Class state was not found for " + state.toString());

        return objFactory.create();
    }

    public abstract void enter(BotContext context);
    public abstract BotStates nextState(BotContext context);
    public abstract BotStates currentState();
    public void handleInput(BotContext context) {}
    public boolean isEnterImmediately() {
        return false;
    }


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

        log.trace("Sending picture:{}", message.toString());
        context.getBot().execute(message);
    }

    @SneakyThrows
    protected void sendMessageRequestPhone(BotContext context, String text) {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("Отправить номер телефона");
        keyboardButton.setRequestContact(true);
        keyboardFirstRow.add(keyboardButton);
        keyboard.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboard);

        SendMessage message = SendMessage.builder()
                .chatId(Long.toString(context.getUser().getChatId()))
                .text(text)
                .replyMarkup(replyKeyboardMarkup)
                .build();

        log.trace("Sending message:{}", message.toString());
        context.getBot().execute(message);
    }

    @SneakyThrows
    protected void sendMessageWithKeyboardRemove(BotContext context, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(Long.toString(context.getUser().getChatId()))
                .text(text)
                .replyMarkup(ReplyKeyboardRemove.builder()
                        .removeKeyboard(true)
                        .build()
                )
                .build();

        log.trace("Sending KeyboardRemove");
        context.getBot().execute(message);
    }
}
