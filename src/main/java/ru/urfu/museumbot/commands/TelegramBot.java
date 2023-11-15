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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import ru.urfu.museumbot.JPA.models.Event;
import ru.urfu.museumbot.JPA.models.Review;
import ru.urfu.museumbot.JPA.models.User;
import ru.urfu.museumbot.JPA.service.ReviewService;
import ru.urfu.museumbot.JPA.service.UserService;
import ru.urfu.museumbot.JPA.service.EventService;

import java.util.ArrayList;
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

    private final EventService eventData;

    private final UserService userService;

    private final ReviewService reviewService;

    @Autowired
    public TelegramBot(EventService eventService, UserService userService, ReviewService reviewService) {
        this.eventData = eventService;
        this.userService = userService;
        this.reviewService = reviewService;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Старт"));
        listOfCommands.add(new BotCommand("/help", "Нужна помощь?"));
        listOfCommands.add(new BotCommand("/view_upcoming_events", "Посмотреть ближайшие мероприятия"));
        listOfCommands.add(new BotCommand("/sign_up_for_event", "Зарегистрироваться на мероприятие"));
        listOfCommands.add(new BotCommand("/cancel", "Отменить запись на мероприятие"));
        listOfCommands.add(new BotCommand("/view_my_events", "Посмотреть на записанные мероприятия"));
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

            switch (messageText) {
                case "/start" -> startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                case "/help" -> sendMessage(chatId, StaticText.HELP_TEXT);
                case "/view_upcoming_events" -> viewUpcomingEvents(chatId);
                case "/sign_up_for_event" -> sendMessage(chatId, "В разработке..");
                case "/cancel" -> cancel(chatId);
                case "/view_my_events" -> viewMyEvents(chatId);
                default -> sendMessage(chatId, "Извините, команда не распознана");
            }
        }
        else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();

            String text = "Вы отменили свою запись на выбранное мероприятие";

            if (callbackData.startsWith("CancelEvent")){
                Long eventId = Long.valueOf(callbackData.replace("CancelEvent", ""));
                Review review = reviewService.getReview(
                        userService.getUserByChatId(chatId),
                        eventData.getEventById(eventId));
                reviewService.deleteReview(review);
            }

            EditMessageText message = new EditMessageText();
            message.setChatId(String.valueOf(chatId));
            message.setText(text);
            message.setMessageId(messageId);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * <p>Посмотреть предстоящие мероприятия</p>
     */
    private void viewUpcomingEvents(long chatId) {
        eventData.getListEvents().forEach(event -> sendMessage(chatId, event.toString()));
    }

    /**
     * <p>Посмотреть события, на которые записан пользователь</p>
     */
    private void viewMyEvents(Long chatId) {
        List<Event> events = userService.getUserEvents(chatId);
        if (events.size() > 0)
            events.forEach(event -> sendMessage(chatId, event.toString()));
        else
            sendMessage(chatId, "Вы ещё не записаны ни на одно мероприятие.");
    }

    /**
     * <p>Отменить запись пользователя на мероприятия</p>
     */
    private void cancel(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберете мероприятие, на которое хотите отменить запись:");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<Event> userEvents = userService.getUserEvents(chatId);
        for (Event event : userEvents) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(event.getTitle());
            inlineKeyboardButton.setCallbackData("CancelEvent" + event.getId());
            rowInline.add(inlineKeyboardButton);
            rowsInline.add(rowInline);
        }
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        executeMessage(message);
    }

    /**
     * Обработчик команды /start
     * @param chatId id чата, необходим для отправки сообщения ботом пользователю
     * @param name имя пользователя
     */
    private void startCommandReceived(long chatId, String name) {
        User newUser = new User();
        newUser.setName(name);
        newUser.setChatId(chatId);
        userService.addUser(newUser);
        String answer = "Здравствуйте, " + name +
                ", я бот, который поможет вам отслеживать предстоящие культурные мероприятия. " +
                StaticText.HELP_TEXT;
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
        executeMessage(message);
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
