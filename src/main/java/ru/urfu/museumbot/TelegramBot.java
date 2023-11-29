package ru.urfu.museumbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.urfu.museumbot.buttons.ButtonsContent;
import ru.urfu.museumbot.commands.CommandContainer;
import ru.urfu.museumbot.jpa.service.*;

import java.util.List;

/**
 * <p>Класс реализации функционала телеграм бота</p>
 */
@Component
public class TelegramBot extends TelegramLongPollingBot implements Bot {

    private final String botName;
    private final String botToken;
    private final CommandContainer commandContainer;

    public TelegramBot(@Value("${bot.name}") String botName,
                       @Value("${bot.token}") String botToken,
                       EventService eventService,
                       ReviewService reviewService,
                       UserService userService) {
        super(botToken);
        this.botToken = botToken;
        this.botName = botName;
        this.commandContainer = new CommandContainer(
                eventService,
                reviewService,
                userService);
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
        SendMessage sendMessage = null;
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            sendMessage = commandContainer.retrieveCommand(message).getMessage(update);
        }
        else if (update.hasCallbackQuery()) {
            // при нажатии на кнопку в зависимости от текста, передаваемого кнопкой, обрабатывается соответсвующая команда
            String callbackData = update.getCallbackQuery().getData().split(" ")[0];
            sendMessage = commandContainer.retrieveCommand(callbackData).getMessage(update);
        }
        sendMessage(sendMessage);
    }

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(this);
        } catch (TelegramApiException e) {
            System.out.println("Не получилось запустить бота. Причина: " + e.getMessage());
        }
    }

    /**
     * Метод для отправки сообщения
     */
    @Override
    public void sendMessage(SendMessage message) {
        try {
            this.execute(message);
        } catch (TelegramApiException e) {
            System.out.printf("Сообщение не отправилось! Причина: %s", e.getMessage());
        }
    }
}
