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

    @Autowired
    final EventService eventService = new EventService();

    @Autowired
    UserService userService;

    @Autowired
    ReviewService reviewService;

    public TelegramBot() {
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
                case "/sign_up_for_event" -> signUp(chatId);
                case "/cancel" -> cancel(chatId);
                case "/view_my_events" -> viewMyEvents(chatId);
                default -> sendMessage(chatId, "Извините, команда не распознана");
            }
        }
       else if (update.hasCallbackQuery()) {
           // при нажатии на кнопку в зависимости от текста передаваемого кнопкой обрабатывается соответсвующая команда
            String callbackData = update.getCallbackQuery().getData();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();

            String text = "";

            if (callbackData.startsWith("CancelEvent")){
                text = cancelReviewCommand(callbackData, chatId);
            }

            if (callbackData.startsWith("AddEvent")){
                text = addReviewCommand(callbackData, chatId);
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
     * <p>Регистрирует на выбранное мероприятие</p>
     * <p>Добавляет в таблицу Review запись о мероприятии на которое записался пользователь</p>
     * * @param callbackData текст идентификации действия получаемый при нажатии на кнопку
     * @return текст, который выводится пользователю при успехе
     */
    private String addReviewCommand(String callbackData, Long chatId) {
        String text;
        text = "Вы записались на выбранное мероприятие";
        Long eventId = Long.valueOf(callbackData.replace("AddEvent", ""));
        Review review = new Review();
        User user = userService.getUserByChatId(chatId);
        Event event = eventService.getEventById(eventId);

        Review prevReview = reviewService.getReview(user, event);
        if(prevReview == null) {
            review.setUser(user);
            review.setEvent(event);
            reviewService.addReview(review);
        }
        else{
            text = String.format("Вы уже записаны на мероприятие \"%s\"", prevReview.getEvent().getTitle());
        }
        return text;
    }

    /**
     * <p>Отменяет запись на выбранное мероприятие</p>
     * <p>Удаляет выбранную запись мероприятия из таблицы Review</p>
     * @param callbackData текст идентификации действия получаемый при нажатии на кнопку
     * @return текст, который выводится пользователю при успехе
     */
    private String cancelReviewCommand(String callbackData, Long chatId) {
        String text;
        text = "Вы отменили свою запись на выбранное мероприятие";
        Long eventId = Long.valueOf(callbackData.replace("CancelEvent", ""));
        Review review = reviewService.getReview(
                userService.getUserByChatId(chatId),
                eventService.getEventById(eventId));
        reviewService.deleteReview(review);
        return text;
    }

    /**
     * <p>Посмотреть предстоящие мероприятия</p>
     */
    private void viewUpcomingEvents(long chatId) {
        eventService.getListEvents().forEach(event ->
                sendMessage(chatId, event.toString()));
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
     * <p>Выводит списком кнопок мероприятия на которые записан пользователь с возможностью отмены записи на мероприятие</p>
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
     * <p>Промежуточное действие перед регистрацией на мероприятие</p>
     * <p>Выводит список ближайших мероприятий в виде кнопок с возможностью для пользователя заипсаться на одно из них</p>
     */
    private void signUp(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберете мероприятие, на которое хотите записаться:");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<Event> allEvents = eventService.getListEvents();

        for (Event event : allEvents) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(event.getTitle());
            inlineKeyboardButton.setCallbackData("AddEvent" + event.getId());
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
