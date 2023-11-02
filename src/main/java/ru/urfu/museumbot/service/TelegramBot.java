package ru.urfu.museumbot.service;

import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import ru.urfu.museumbot.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.urfu.museumbot.models.Event;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * <p>Класс реализации функционала телеграм бота</p>
 * <p>{@link TelegramBot#config} конфигурация бота, содержит настрйоки для подключения к боту</p>
 */
@Component
public class TelegramBot extends TelegramLongPollingBot {

    public static final int AMOUNT_TO_ADD_DATE = 7;
    final BotConfig config;

    final EventController eventData;

    public TelegramBot(BotConfig config, EventController eventData) {
        this.config = config;
        this.eventData = eventData;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Старт"));
        listOfCommands.add(new BotCommand("/help", "Нужна помощь?"));
        listOfCommands.add(new BotCommand("/view_upcoming_events", "Посмотреть ближайшие мероприятия"));
        listOfCommands.add(new BotCommand("/sign_up_for_event", "Зарегистрироваться на мероприятие"));
        try{
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        }
        catch (TelegramApiException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
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

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    sendMessage(chatId, "загрушка: помощь");
                    break;
                case "/view_upcoming_events":
                    viewUpcomingEvents(chatId);
                    break;
                case "/sign_up_for_event":
                    sendMessage(chatId, "В разработке..");
                    break;
                default:
                    sendMessage(chatId, "Извините, команда не распознана");
            }
        }


    }

    private void viewUpcomingEvents(long chatId) {
        Instant now = new Date().toInstant();
        Instant dateTo  = now.plus(AMOUNT_TO_ADD_DATE, ChronoUnit.DAYS);
        eventData.getListEvents().stream().filter(event -> event.getDate().
                toInstant().isBefore(dateTo) && event.getDate().toInstant().isAfter(now)).sorted(new Comparator<Event>() {
                    @Override
                    public int compare(Event o1, Event o2) {
                        return o1.getDate().compareTo(o2.getDate());
                    }
                })
                .forEach(event -> sendMessage(chatId, event.toString()));
    }

    /**
     * Обработчик команды /start
     * @param chatId id чата, необходим для отправки сообщения ботом пользователю
     * @param name имя пользователя
     */
    private void startCommandReceived(long chatId, String name) {


        String answer = "Привет, " + name + ", я бот для мезеев!";


        sendMessage(chatId, answer);
    }

    /**
     * Метод для отправки ботом сообщения
     * @param chatId id чата, необходим для идентификации в какой чат отправляем
     * @param textToSend сообщение
     */
    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {

        }
    }
}
