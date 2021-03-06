package com.professional.telegram_hyecorn_bot.service;

import com.professional.telegram_hyecorn_bot.BotContext;
import com.professional.telegram_hyecorn_bot.BotStates.BotStates;
import com.professional.telegram_hyecorn_bot.BotStates.StateAbstract;
import com.professional.telegram_hyecorn_bot.model.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;

import java.util.Optional;

@Service
@Slf4j
public class ChatBot extends TelegramWebhookBot {

    private final String token;
    private final String botUsername;

    private final UserService userService;

    public ChatBot(@Value("${telegram.token}") String token,
                   @Value("${telegram.username}") String botUsername,
                   UserService userService) {
        super();
        this.token = token;
        this.botUsername = botUsername;
        this.userService = userService;

        log.trace("Bot {} started", botUsername);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    public void onUpdateReceived(Update update) {
        if (checkCommands(update))
            return;

        String text = null;
        long chatId = 0;
        String firstName = null;
        String lastName = null;
        Contact contact = null;
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.trace("INCOMING CallbackQuery:{}", callbackQuery.toString());
            chatId = callbackQuery.getFrom().getId();
        }
        if (update.hasMessage()) {
            Message message = update.getMessage();
            chatId = message.getChatId();
            text = message.getText();
            firstName = message.getChat().getFirstName();
            lastName = message.getChat().getLastName();
            contact = message.getContact();
            log.trace("INCOMING Message:{}", message.toString());
        }

        User user = userService.findByChatId(chatId);
        BotContext botContext;
        StateAbstract userState;

        //???????????????? ???? ???? ?????????? ???????????????????????? ???? ???????? ???????? ?? ???????? ???????? ????????????????
        if (user == null) {
            userState = StateAbstract.create(BotStates.getInitialState());
            user = new User(chatId, userState.toString(), firstName, lastName);
            userService.createUser(user);

            botContext = new BotContext(this, user, text, contact);
            userState.enter(botContext);
        } else {
            //???????? ?????????? ????????????????????????, ?????????????????? ?????? ??????????????????
            botContext = new BotContext(this, user, text, contact);
            userState = StateAbstract.create(BotStates.fromValue(user.getUserState()));
        }

        //???????????????????????? ?????? ???????? ????????????????????????
        userState.handleInput(botContext);
        do {
            userState = StateAbstract.create(userState.nextState(botContext));
            userState.enter(botContext);
        } while (userState.isEnterImmediately());

        user.setUserState(userState.currentState().toString());
        userService.updateUser(user);
    }

    @SneakyThrows
    private boolean checkCommands(Update update) {
        if (update.hasMessage() && (update.getMessage() != null)) {
            Message message = update.getMessage();
            if (message.getEntities() != null) {

                Optional<MessageEntity> commandEntity =
                        message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
                if (commandEntity.isPresent()) {
                    String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());

                    //TODO ?????????? ????????????
                    if ("/deleteMe".equalsIgnoreCase(command)) {
                        log.trace("INCOMING /deleteMe:{}", message.toString());
                        Long userId = userService.findByChatId(message.getChatId()).getId();
                        if (userId != null) {
                            userService.deleteUser(userId);
                        }
                        return true;
                    }

                    if ("/support".equalsIgnoreCase(command)) {
                        log.trace("INCOMING /support:{}", message.toString());

                        SendMessage messageToSend = SendMessage.builder()
                                .chatId(Long.toString(message.getChatId()))
                                .text("???????????????? @HyecornSupportBot")
                                .build();
                        this.execute(messageToSend);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        log.info("chatbot/onWebhookUpdateReceived");
        return null;
    }

    @Override
    public String getBotPath() {
        return null;
    }
}
