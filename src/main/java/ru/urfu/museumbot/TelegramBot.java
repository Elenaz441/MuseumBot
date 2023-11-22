package ru.urfu.museumbot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import ru.urfu.museumbot.buttons.ButtonsContent;
import ru.urfu.museumbot.commands.CommandContainer;
import ru.urfu.museumbot.jpa.service.SendBotMessageService;
import ru.urfu.museumbot.jpa.service.ServiceContext;

import java.util.List;

/**
 * <p>Класс реализации функционала телеграм бота</p>
 */
public class TelegramBot extends TelegramLongPollingBot {

    private final String botName;
    private final String botToken;
    private final CommandContainer commandContainer;

    public TelegramBot(String botToken, String botName, ServiceContext serviceContext) {
        super(botToken);
        this.botToken = botToken;
        this.botName = botName;
        this.commandContainer = new CommandContainer(new SendBotMessageService(this), serviceContext);
        List<BotCommand> listOfCommands = new ButtonsContent().getMenuOfCommands();
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
            String message = update.getMessage().getText();
            commandContainer.retrieveCommand(message).execute(update);
        }
        else if (update.hasCallbackQuery()) {
            // при нажатии на кнопку в зависимости от текста, передаваемого кнопкой, обрабатывается соответсвующая команда
            String callbackData = update.getCallbackQuery().getData().split(" ")[0];
            commandContainer.retrieveCommand(callbackData).execute(update);
        }
    }
}
