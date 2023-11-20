package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.urfu.museumbot.GUI.Widgets;
import ru.urfu.museumbot.JPA.models.*;
import ru.urfu.museumbot.JPA.service.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static ru.urfu.museumbot.commands.Commands.*;


/**
 * Класс логики
 */
@Component
public class BotLogic {

    private final EventService eventService;

    private final UserService userService;

    private final ReviewService reviewService;

    private final ExhibitService exhibitService;

    private final MuseumService museumService;

    private final Widgets gui;

    /**
     * Создание логики бота
     */
    @Autowired
    public BotLogic(EventService eventService, UserService userService, ReviewService reviewService, MuseumService museumService, ExhibitService exhibitService) {
        this.eventService = eventService;
        this.userService = userService;
        this.reviewService = reviewService;
        this.museumService = museumService;
        this.exhibitService = exhibitService;
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
            case VIEW_EXHIBIT -> message = viewExhibit(chatId);
            case LEAVE_REVIEW -> message = leaveReview(chatId);
            case VIEW_MUSEUM -> message = viewMuseums(chatId, GET_MUSEUM);
            case VIEW_MUSEUM_RANK -> message = viewMuseums(chatId, GET_RANK);
            default -> message = new SendMessage(String.valueOf(chatId), "Извините, команда не распознана");
        }
        return message;
    }

    /**
     * СОбработчик команды оставить отзыв
     * @param chatId идентификатор чата
     * @return сообщение от бота
     */
    private SendMessage leaveReview(Long chatId) {
        SendMessage message = new SendMessage();
        List<Event> events = userService.getAllVisitedEvents(chatId);
        Map<Long, String> visitedEvents = events
                .stream()
                .collect(Collectors.toMap(Event::getId, Event::getTitle));
        message.setChatId(chatId);
        message.setText("Выберите мероприятие:\n");
        message.setReplyMarkup(gui.getMarkupInline(LEAVE_REVIEW, visitedEvents));
        return message;
    }

    /**
     * Получить информацию об экспонатах мероприятия,
     * на котором пользователь находится сейчас
     * информацию можно получить только в течение мероприятия
     * @param chatId идентификатор чата
     * @return сформированный ответ от бота
     */
    private SendMessage viewExhibit(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        List<Event> usersEvents = userService.getUserEvents(chatId);
        Optional<Event> eventInActive = isUserAtEvent(usersEvents);
        if(eventInActive.isPresent()){
                List<Exhibit> stualExhibits = eventInActive.get().getMuseum().getExhibits();
            Map<Long, String> variants = stualExhibits
                    .stream()
                    .collect(Collectors.toMap(Exhibit::getId, Exhibit::getTitle));
            InlineKeyboardMarkup markup = gui.getMarkupInline(VIEW_EXHIBIT, variants);
            message.setText("Чтобы получить информацию выберете экспонат");
            message.setReplyMarkup(markup);
        }
        else {
            message.setText("Выставка ещё не началась. Эта команда недоступна");
        }
        return message;
    }

    /**
     * @param usersEvents список мероприятий, на которые записан пользователь
     * @return мероприятие, которое в процессе
     */
    private Optional<Event> isUserAtEvent(List<Event> usersEvents) {
        Instant now = Instant.now();
        Event result = null;
        for(Event event: usersEvents){
            Instant eventDate = event.getDate().toInstant();
            if(now.isAfter(eventDate) && now.isBefore(eventDate.plus(event.getDuration(), ChronoUnit.MINUTES))) {
                result = event;
            }
        }
        return Optional.ofNullable(result);
    }

    /**
     * Обработать нажатие кнопки
     */
    public EditMessageText handleCallbackQuery(String callbackData, Integer messageId, Long chatId) {
        String text;
        String commandName = callbackData.split(" ")[0];
        Long commandContext = Long.valueOf(callbackData.split(" ")[1]);
        switch (commandName){
            case ADD_EVENT ->  text = addReviewCommand(commandContext, chatId);
            case CANCEL_EVENT -> text = cancelReviewCommand(commandContext, chatId);
            case VIEW_EXHIBIT -> text = viewExhibitCommand(commandContext);
            case LEAVE_REVIEW -> text = leaveReviewCommand(commandContext);
            case GET_MUSEUM -> text = getMuseumCommand(commandContext);
            case GET_RANK -> text = getMuseumRankCommand(commandContext);
            default -> text = "Что-то пошло не так, попробуйте позже.";
        }
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId(messageId);
        return message;
    }

    /**
     * Выполняет добавление оценки и комментария к отзову
     * @param commandContext идентификатор отзыва
     * @return сообщение от бота об успешном выполнении команды
     */
    private String leaveReviewCommand(Long commandContext) {
        //TODO
        return null;
    }

    private String viewExhibitCommand(Long exhibitId) {
        System.out.println(exhibitService.getExhibitById(exhibitId));
        return exhibitService.getExhibitById(exhibitId).toString();
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
        Map<Long, String> variants = allEvents
                .stream()
                .collect(Collectors.toMap(Event::getId, Event::getTitle));
        InlineKeyboardMarkup markupInline = gui.getMarkupInline(ADD_EVENT, variants);
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
        Map<Long, String> variants = userEvents
                .stream()
                .collect(Collectors.toMap(Event::getId, Event::getTitle));
        InlineKeyboardMarkup markupInline = gui.getMarkupInline(CANCEL_EVENT, variants);
        message.setReplyMarkup(markupInline);
        return message;
    }

    /**
     * <p>Регистрирует на выбранное мероприятие</p>
     * <p>Добавляет в таблицу Review запись о мероприятии на которое записался пользователь</p>
     * @param eventId идентификатор мероприятия
     * @return текст, который выводится пользователю при успехе
     */
    private String addReviewCommand(Long eventId, Long chatId) {
        String text = "Вы записались на выбранное мероприятие";
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
     * @param eventId идентификатор мероприятия
     * @return текст, который выводится пользователю при успехе
     */
    private String cancelReviewCommand(Long eventId, Long chatId) {
        String text = "Вы отменили свою запись на выбранное мероприятие";
        Review review = reviewService.getReview(
                userService.getUserByChatId(chatId),
                eventService.getEventById(eventId));
        reviewService.deleteReview(review);
        return text;
    }

    /**
     * Посмотреть список музеев
     */
    private SendMessage viewMuseums(Long chatId, String command) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберете музей:");
        List<Museum> allMuseums = museumService.getMuseums();
        Map<Long, String> variants = allMuseums
                .stream()
                .collect(Collectors.toMap(Museum::getId, Museum::getTitle));
        InlineKeyboardMarkup markupInline = gui.getMarkupInline(command, variants);
        message.setReplyMarkup(markupInline);
        return message;
    }

    /**
     * Получить информацию о музее
     */
    private String getMuseumCommand(Long museumId) {
        List<Event> events = museumService.getUpcomingEvents(museumId);
        String textEvents = events
                .stream()
                .map(Event::toString)
                .collect(Collectors.joining("\n\n===============================\n\n"));
        return museumService.getMuseumById(museumId).toString() + "\n\nБлижайшие мероприятия:\n" + textEvents;
    }

    /**
     * Получить рейтинг музея (среднюю оценку пользователей) и последние 10 отзывов (или меньше, если их меньше 10)
     */
    private String getMuseumRankCommand(Long museumId) {
        String text = "Средняя оценка от пользователей: ";
        List<Review> reviews = museumService.getMuseumReviews(museumId);
        String rank = museumService.getMuseumRank(museumId);
        if (reviews.size() > 10) {
            reviews = reviews.subList(0, 11);
        }
        String textReviews = reviews
                .stream()
                .map(Review::toString)
                .collect(Collectors.joining("\n\n===============================\n\n"));
        return text + rank + ". Ниже несколько последних отзывов: \n" + textReviews;
    }
}
