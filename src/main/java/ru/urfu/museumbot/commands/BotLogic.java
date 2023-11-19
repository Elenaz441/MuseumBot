package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.museumbot.GUI.Widgets;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.urfu.museumbot.commands.Commands.*;


/**
 * Класс логики
 */
@Component
public class BotLogic {


    private final EventService eventService;

    private final UserService userService;

    private final ReviewService reviewService;
    private final Widgets gui;

    /**
     * Создание логики бота
     */
    @Autowired
    public BotLogic(EventService eventService, UserService userService, ReviewService reviewService) {
        this.eventService = eventService;
        this.userService = userService;
        this.reviewService = reviewService;
        this.gui = new Widgets();

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
            case START -> message = startCommandReceived(chatId, username);
            case HELP -> message = new SendMessage(String.valueOf(chatId), StaticText.HELP_TEXT);
            case VIEW_UPCOMING_EVENTS -> message = viewUpcomingEvents(chatId);
            case SIGN_UP_FOR_EVENT -> message = signUp(chatId);
            case CANCEL -> message = cancel(chatId);
            case VIEW_MY_EVENTS -> message = viewMyEvents(chatId);
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
        List<Event> allEvents = eventService.getListEvents();
        InlineKeyboardMarkup markupInline = gui.getMarkupInline("AddEvent", allEvents);
        message.setReplyMarkup(markupInline);
        return message;
    }

    /**
     * <p>Отменить запись пользователя на мероприятия</p>
     */
    private SendMessage cancel(Long chatId) {
        SendMessage message = new SendMessage();
        String text = "Выберете мероприятие, на которое хотите отменить запись:";
        List<Event> userEvents = userService.getUserEvents(chatId);
        if (userEvents.size() == 0) {
            text = "Вы ещё не записаны ни на одно мероприятие.";
        }
        message.setChatId(chatId);
        message.setText(text);
        InlineKeyboardMarkup markupInline = gui.getMarkupInline("CancelEvent", userEvents);
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
