package ru.urfu.museumbot.commands;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.museumbot.JPA.models.Event;
import ru.urfu.museumbot.JPA.models.Review;
import ru.urfu.museumbot.JPA.models.User;
import ru.urfu.museumbot.JPA.service.EventService;
import ru.urfu.museumbot.JPA.service.ReviewService;
import ru.urfu.museumbot.JPA.service.UserService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Класс логики
 */
@Component
public class BotLogic {

    private final EventService eventService;

    private final UserService userService;

    private final ReviewService reviewService;

    /**
     * Создание логики бота
     */
    @Autowired
    public BotLogic(EventService eventService, UserService userService, ReviewService reviewService) {
        this.eventService = eventService;
        this.userService = userService;
        this.reviewService = reviewService;
    }

    /**
     * Обработать входящее текстовое сообщение
     * @param messageText - текст от пользователя
     * @param chatId - id чата пользователя
     * @param username - имя пользователя
     */
    public SendMessage handleIncomingTextMessage(String messageText, Long chatId, String username) {
        SendMessage message;
        switch (messageText) {
            case "/start" -> message = startCommandReceived(chatId, username);
            case "/help" -> message = new SendMessage(String.valueOf(chatId), StaticText.HELP_TEXT);
            case "/view_upcoming_events" -> message = viewUpcomingEvents(chatId);
            case "/sign_up_for_event" -> message = signUp(chatId);
            case "/cancel" -> message = cancel(chatId);
            case "/view_my_events" -> message = viewMyEvents(chatId);
            case "/viewExhibit" -> message = viewExhibit(chatId);
            default -> message = new SendMessage(String.valueOf(chatId), "Извините, команда не распознана");
        }
        return message;
    }

    private SendMessage viewExhibit(Long chatId) {
        String text = "";
        SendMessage message = new SendMessage();
        List<Event> usersEvents = userService
                .getUserEvents(chatId)
                .stream().collect(Collectors.toList());
        Optional<Event> eventInActive = isUserAtEvent(usersEvents);
        if(eventInActive.isPresent()){
            //need inline keyboard
        }
        else {
            text = "Выставка ещё не началась. Эта команда недоступна";
        }
        return message;
    }

    private Optional<Event> isUserAtEvent(List<Event> usersEvents) {
        Instant now = Instant.now();
        Event result = null;
        for(Event event: usersEvents){
            Instant eventDate = event.getDate().toInstant();
            if(eventDate.isAfter(now) && eventDate.plus(event.getDuration(), ChronoUnit.MINUTES).isBefore(now)) {
                result = event;
            }
        }
        return Optional.ofNullable(result);
    }

    /**
     * Обработать нажатие кнопки
     */
    public EditMessageText handleCallbackQuery(String callbackData, Integer messageId, Long chatId) {
        String text = "Что-то пошло не так, попробуйте позже.";

        if (callbackData.startsWith("AddEvent")){
            text = addReviewCommand(callbackData, chatId);
        }

        if (callbackData.startsWith("CancelEvent")){
            text = cancelReviewCommand(callbackData, chatId);
        }

        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId(messageId);
        return message;
    }

    /**
     * Обработчик команды /start
     * @param chatId id чата, необходим для отправки сообщения ботом пользователю
     * @param name имя пользователя
     */
    private SendMessage startCommandReceived(long chatId, String name) {
        User newUser = new User();
        newUser.setName(name);
        newUser.setChatId(chatId);
        userService.addUser(newUser);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Здравствуйте, " + name +
                ", я бот, который поможет вам отслеживать предстоящие культурные мероприятия. " +
                StaticText.HELP_TEXT);
        return message;
    }

    /**
     * <p>Посмотреть предстоящие мероприятия</p>
     */
    private SendMessage viewUpcomingEvents(long chatId) {
        String text = eventService
                .getListEvents()
                .stream()
                .map(Event::toString)
                .collect(Collectors.joining("\n\n===============================\n\n"));
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        return message;
    }

    /**
     * <p>Посмотреть события, на которые записан пользователь</p>
     */
    private SendMessage viewMyEvents(Long chatId) {
        String text = userService
                .getUserEvents(chatId)
                .stream()
                .map(Event::toString)
                .collect(Collectors.joining("\n\n===============================\n\n"));
        if (text.length() == 0) {
            text = "Вы ещё не записаны ни на одно мероприятие.";
        }
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        return message;
    }

    /**
     * <p>Промежуточное действие перед регистрацией на мероприятие</p>
     * <p>Выводит список ближайших мероприятий в виде кнопок с возможностью для пользователя записаться на одно из них</p>
     */
    private SendMessage signUp(Long chatId) {
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
        return message;
    }

    /**
     * <p>Отменить запись пользователя на мероприятия</p>
     */
    private SendMessage cancel(Long chatId) {
        SendMessage message = new SendMessage();
        String text = "Выберете мероприятие, на которое хотите отменить запись:";

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

        if (userEvents.size() == 0) {
            text = "Вы ещё не записаны ни на одно мероприятие.";
        }

        message.setChatId(chatId);
        message.setText(text);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        return message;
    }

    /**
     * <p>Регистрирует на выбранное мероприятие</p>
     * <p>Добавляет в таблицу Review запись о мероприятии на которое записался пользователь</p>
     * * @param callbackData текст идентификации действия получаемый при нажатии на кнопку
     * @return текст, который выводится пользователю при успехе
     */
    private String addReviewCommand(String callbackData, Long chatId) {
        String text = "Вы записались на выбранное мероприятие";
        Long eventId = Long.valueOf(callbackData.replace("AddEvent", ""));
        Review review = new Review();
        User user = userService.getUserByChatId(chatId);
        Event event = eventService.getEventById(eventId);

        Review prevReview = reviewService.getReview(user, event);
        if (prevReview == null) {
            review.setUser(user);
            review.setEvent(event);
            reviewService.addReview(review);
        }
        else {
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
        String text = "Вы отменили свою запись на выбранное мероприятие";
        Long eventId = Long.valueOf(callbackData.replace("CancelEvent", ""));
        Review review = reviewService.getReview(
                userService.getUserByChatId(chatId),
                eventService.getEventById(eventId));
        reviewService.deleteReview(review);
        return text;
    }
}
