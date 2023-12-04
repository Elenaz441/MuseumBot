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
import ru.urfu.museumbot.buttons.ButtonContent;
import ru.urfu.museumbot.buttons.ButtonsContext;
import ru.urfu.museumbot.commands.Command;
import ru.urfu.museumbot.commands.CommandArgs;
import ru.urfu.museumbot.commands.CommandContainer;
import ru.urfu.museumbot.message.Message;

import java.util.List;

/**
 * <p>Класс реализации функционала телеграм бота</p>
 */
@Component
public class TelegramBot extends TelegramLongPollingBot implements Bot {

    private final String botName;
    private final String botToken;
    private final CommandContainer commandContainer;
    private final ButtonContent buttonContent;

    public TelegramBot(@Value("${bot.name}") String botName,
                       @Value("${bot.token}") String botToken,
                       CommandContainer commandContainer) {
        super(botToken);
        this.botToken = botToken;
        this.botName = botName;
        this.commandContainer = commandContainer;
        this.buttonContent = new ButtonContent();
        List<BotCommand> listOfCommands = buttonContent.getMenuOfCommands();
        try{
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        }
        catch (TelegramApiException e){
            System.out.println("Не получилось установить меню с командами. Причина: " + e.getMessage());
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
        Message message = null;
        CommandArgs args = new CommandArgs();
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            args.setUserInput(messageText);
            Long chatId = update.getMessage().getChatId();
            args.setChatId(chatId);
            message = commandContainer.retrieveCommand(chatId, messageText).getMessage(args);
        }
        else if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            args.setChatId(chatId);
            String callbackData = update.getCallbackQuery().getData();
            args.setCallbackData(callbackData);
            // при нажатии на кнопку в зависимости от текста, передаваемого кнопкой, обрабатывается соответсвующая команда
            String commandIdentifier = callbackData.split(" ")[0];
            message = commandContainer.retrieveCommand(chatId, commandIdentifier).getMessage(args);
        }
        assert message != null;
        sendMessage(message);
    }

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(this);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Не получилось запустить бота. Причина: " + e.getMessage());
        }
    }

    /**
     * Метод для отправки сообщения
     */
    @Override
    public void sendMessage(Message message) {
        SendMessage sendMessage = getSendMessage(message);
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Сообщение не отправилось! Причина: %s" + e.getMessage());
        }
    }

    /**
     * @param message сообщение, которое должен отправить бот
     * @return сообщение библиотеки telegrambots
     */
    private SendMessage getSendMessage(Message message) {
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), message.getText());
        if (message.getButtonsContext().isPresent()) {
            ButtonsContext buttonsContext = message.getButtonsContext().get();
            sendMessage.setReplyMarkup(buttonContent.getMarkupInline(
                    buttonsContext.getCallbackData(),
                    buttonsContext.getVariants()));
        }
        return sendMessage;
    }
}
