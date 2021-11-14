package com.professional.telegram_hyecorn_bot;

import com.professional.telegram_hyecorn_bot.model.User;
import com.professional.telegram_hyecorn_bot.service.UserService;
import lombok.SneakyThrows;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;

import java.util.Objects;
import java.util.Optional;

@Component
public class ChatBot extends TelegramLongPollingSessionBot {

    private final String token;
    private final String botUsername;

    private final UserService userService;

    public ChatBot(@Value("${telegram.token}") String token,
                   @Value("${telegram.username}") String botUsername,
                   UserService userService) {
        this.token = token;
        this.botUsername = botUsername;
        this.userService = userService;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update, Optional<Session> botSession) {
        if (checkIfSupportCommand(update))
            return;

        String text = null;
        long chatId = 0;
        String firstName = null;
        String lastName = null;
        if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getFrom().getId();
        }
        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            text = update.getMessage().getText();
            firstName = update.getMessage().getChat().getFirstName();
            lastName = update.getMessage().getChat().getLastName();
        }

        User user = userService.findByChatId(chatId);

        BotContext botContext;
        BotState state;

        //Возможно ли не найти пользователя по чату если у него есть кнопка????
        if (user == null) {
            state = BotState.getInitialState();
            user = new User(chatId, state.ordinal(), firstName, lastName);
            userService.updateUser(user);
            botContext = new BotContext(this, user, text);

            state.enter(botContext);
        } else {
            //если нашли пользователя, получения его состояния
            botContext = new BotContext(this, user, text);
            state = BotState.byId(user.getStateId());
        }
        //обрабатываем что ввел пользователь
        state.handleInput(botContext);

        do {
            state = state.nextState(botContext);
            state.enter(botContext);
        } while (state.isEnterImmediately());


        user.setStateId(state.ordinal());
        userService.updateUser(user);
    }

    @SneakyThrows
    private boolean checkIfSupportCommand(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();

            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                if (Objects.equals(command, "/support")) {
                    SendMessage messageToSend = SendMessage.builder()
                            .chatId(Long.toString(message.getChatId()))
                            .text("Напишите @HyecornSupportBot")
                            .build();
                    this.execute(messageToSend);
                    return true;
                }
            }
        }
        return false;
    }
}
