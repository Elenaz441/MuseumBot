package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import ru.urfu.museumbot.GUI.Widgets;

import java.util.List;



/**
 * <p>Класс реализации функционала телеграм бота</p>
 */
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    String botName;

    @Value("${bot.token}")
    String botToken;

    private final BotLogic logic;

    @Autowired
    public TelegramBot(BotLogic logic) {
        this.logic = logic;
        List<BotCommand> listOfCommands = new Widgets().getMenuOfCommands();
        try{
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        }
        catch (TelegramApiException e){
            System.out.println(e.getMessage());
        }
    }



    @Override
    public String getBotUsername() {
        return this.botName;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    /**
     * <p>вызывается каждый раз при отправке сообщения пользователем.</p>
     * @param update параметр, с помощью которого мы можем получить текст сообщения
     */
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getChat().getFirstName();
            SendMessage message = logic.handleIncomingTextMessage(messageText, chatId, username);
            executeMessage(message);
        }
        else if (update.hasCallbackQuery()) {
            // при нажатии на кнопку в зависимости от текста, передаваемого кнопкой, обрабатывается соответсвующая команда
            String callbackData = update.getCallbackQuery().getData();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            EditMessageText messageText = logic.handleCallbackQuery(callbackData, messageId, chatId);
            executeEditMessage(messageText);
        }
    }

    /**
     * Осуществляет отправку сообщения
     */
    private void executeEditMessage(EditMessageText message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * <p>Осуществить отправку сообщения</p>
     * @param message - сообщение
     */
    private void executeMessage(SendMessage message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }
}
