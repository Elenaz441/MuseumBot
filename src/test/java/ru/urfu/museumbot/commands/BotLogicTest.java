package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.urfu.museumbot.JPA.models.Event;
import ru.urfu.museumbot.JPA.models.Museum;
import ru.urfu.museumbot.JPA.models.Review;
import ru.urfu.museumbot.JPA.models.User;
import ru.urfu.museumbot.JPA.service.EventService;
import ru.urfu.museumbot.JPA.service.MuseumService;
import ru.urfu.museumbot.JPA.service.ReviewService;
import ru.urfu.museumbot.JPA.service.UserService;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для тестирования класса {@link BotLogic}
 */
@ExtendWith(MockitoExtension.class)
class BotLogicTest {

    @InjectMocks
    BotLogic logic;

    @Mock
    UserService userService;

    @Mock
    EventService eventService;

    @Mock
    ReviewService reviewService;

    @Mock
    MuseumService museumService;

    /**
     * Тест для функции /view_upcoming_events
     */
    @Test
    void handleIncomingTextMessage_ViewUpcomingEvents() {
        List<Event> events = getEvents();
        Mockito.doReturn(events)
                .when(eventService)
                .getListEvents();

        SendMessage message = logic.handleIncomingTextMessage(
                "/view_upcoming_events",
                1L,
                "Pon");
        assertEquals(events.get(0) + "\n\n===============================\n\n" + events.get(1), message.getText());
    }

    /**
     * Тест для функции /view_my_events
     */
    @Test
    void handleIncomingTextMessage_ViewMyEvents() {
        List<Event> events = getEvents();
        Long chatId = 1L;
        Mockito.doReturn(events)
                .when(userService)
                .getUserEvents(chatId);

        SendMessage message = logic.handleIncomingTextMessage(
                "/view_my_events",
                chatId,
                "Pomni");
        assertEquals(events.get(0) + "\n\n===============================\n\n" + events.get(1), message.getText());
    }

    /**
     * Тест для функции /sign_up_for_event
     */
    @Test
    void handleIncomingTextMessage_SignUp() {
        List<Event> events = getEvents();
        Mockito.doReturn(events)
                .when(eventService)
                .getListEvents();

        SendMessage message = logic.handleIncomingTextMessage(
                "/sign_up_for_event",
                1L,
                "Robin");

        assertEquals("Выберете мероприятие, на которое хотите записаться:", message.getText());
        assertEquals(InlineKeyboardMarkup.class,  message.getReplyMarkup().getClass());

        InlineKeyboardMarkup keyboard = (InlineKeyboardMarkup) message.getReplyMarkup();

        assertEquals(2, keyboard.getKeyboard().size());
        assertEquals(1, keyboard.getKeyboard().get(0).size());
        assertEquals("Event 1", keyboard.getKeyboard().get(0).get(0).getText());
    }

    /**
     * Тест для функции /cancel
     */
    @Test
    void handleIncomingTextMessage_cancel() {
        List<Event> events = getEvents();
        Long chatId = 1L;
        Mockito.doReturn(events)
                .when(userService)
                .getUserEvents(chatId);

        SendMessage message = logic.handleIncomingTextMessage(
                "/cancel",
                chatId,
                "Lucy");

        assertEquals("Выберете мероприятие, на которое хотите отменить запись:", message.getText());
        assertEquals(InlineKeyboardMarkup.class,  message.getReplyMarkup().getClass());

        InlineKeyboardMarkup keyboard = (InlineKeyboardMarkup) message.getReplyMarkup();

        assertEquals(2, keyboard.getKeyboard().size());
        assertEquals(1, keyboard.getKeyboard().get(0).size());
        assertEquals("Event 1", keyboard.getKeyboard().get(0).get(0).getText());
    }

    /**
     * Тест при вводе некорректной команды
     */
    @Test
    void handleIncomingTextMessage_IncorrectCommand() {
        SendMessage message = logic.handleIncomingTextMessage("Привет!", 1L, "Charlie");
        assertEquals("Извините, команда не распознана", message.getText());
    }

    /**
     * Тест на проверку запросов, где нужно вывести список музеев
     */
    @Test
    void handleIncomingTextMessage_viewMuseums() {
        List<Museum> museums = getMuseums();
        Mockito.doReturn(museums)
                .when(museumService)
                .getMuseums();
        SendMessage message = logic.handleIncomingTextMessage("/view_museum", 77L, "Rufus");

        assertEquals("Выберете музей:", message.getText());

        InlineKeyboardMarkup keyboard = (InlineKeyboardMarkup) message.getReplyMarkup();

        assertEquals(2, keyboard.getKeyboard().size());
        assertEquals(1, keyboard.getKeyboard().get(0).size());
        assertEquals("Museum 1", keyboard.getKeyboard().get(0).get(0).getText());
    }

    /**
     * Тест на регистрацию пользователя на выбранное мероприятие
     */
    @Test
    void handleCallbackQuery_addEvent() {
        Event event = new Event();
        User user = new User();
        Mockito.doReturn(user).when(userService).getUserByChatId(1L);
        Mockito.doReturn(event).when(eventService).getEventById(1L);

        EditMessageText messageText = logic.handleCallbackQuery("AddEvent 1", 111, 1L);
        assertEquals("Вы записались на выбранное мероприятие", messageText.getText());
    }

    /**
     * Тест на регистрацию пользователя на выбранное мероприятие, если пользователь уже зарегистрирован
     */
    @Test
    void handleCallbackQuery_addEventIfSignedUp() {
        Event event = new Event();
        event.setTitle("Event 1");

        User user = new User();

        Review review = new Review();
        review.setEvent(event);
        review.setUser(user);

        Mockito.doReturn(user).when(userService).getUserByChatId(1L);
        Mockito.doReturn(event).when(eventService).getEventById(1L);
        Mockito.doReturn(review).when(reviewService).getReview(user, event);

        EditMessageText messageText = logic.handleCallbackQuery("AddEvent 1", 111, 1L);
        assertEquals("Вы уже записаны на мероприятие \"Event 1\"", messageText.getText());
    }

    /**
     * Тест на отмену регистрации пользователя на выбранное мероприятие
     */
    @Test
    void handleCallbackQuery_cancelEvent() {
        Event event = new Event();
        event.setTitle("Event 1");

        User user = new User();
        user.setChatId(1L);

        Review review = new Review();
        review.setEvent(event);
        review.setUser(user);

        Mockito.doReturn(user).when(userService).getUserByChatId(1L);
        Mockito.doReturn(event).when(eventService).getEventById(1L);
        Mockito.doReturn(review).when(reviewService).getReview(user, event);

        EditMessageText messageText = logic.handleCallbackQuery("CancelEvent 1", 111, 1L);
        assertEquals("Вы отменили свою запись на выбранное мероприятие", messageText.getText());
    }

    /**
     * Тест для проверки функции /view_museum_rank
     */
    @Test
    void handleCallbackQuery_getMuseumCommand() {
        Museum museum = new Museum();
        museum.setId(1L);
        List<Event> events = getEvents();
        museum.setEvents(events);

        User user1 = new User();
        User user2 = new User();

        Review review1 = new Review();
        review1.setUser(user1);
        review1.setEvent(events.get(0));
        review1.setRating(5);
        review1.setReview("Review 1");
        Review review2 = new Review();
        review2.setUser(user2);
        review2.setEvent(events.get(1));
        review2.setRating(7);
        review2.setReview("Review 2");

        Mockito.doReturn(List.of(review1, review2))
                .when(museumService)
                .getMuseumReviews(1L);
        Mockito.doReturn("6.5")
                .when(museumService)
                .getMuseumRank(1L);

        EditMessageText messageText = logic.handleCallbackQuery("GetRank 1", 120, 1L);
        assertEquals("""
                Средняя оценка от пользователей: 6.5. Ниже несколько последних отзывов:\s
                Мероприятие: Event 1
                Оценка: 5
                Отзыв: Review 1
                
                ===============================
                
                Мероприятие: Event 2
                Оценка: 7
                Отзыв: Review 2""",
                messageText.getText());
    }

    /**
     * Получить список мероприятий
     * @return - список мероприятий
     */
    List<Event> getEvents() {
        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle("Event 1");
        event1.setDescription("Descript");
        event1.setDate(new Date());
        event1.setDuration(60);
        event1.setAddress("Ленина, 51");

        Event event2 = new Event();
        event2.setId(2L);
        event2.setTitle("Event 2");
        event2.setDescription("Descript");
        event2.setDate(new Date());
        event2.setDuration(60);
        event2.setAddress("Ленина, 52");

        return List.of(event1, event2);
    }

    /**
     * Получить список музеев
     * @return - список музеев
     */
    List<Museum> getMuseums() {
        Museum museum1 = new Museum();
        museum1.setId(1L);
        museum1.setTitle("Museum 1");
        museum1.setDescription("Descript");
        museum1.setAddress("Ленина, 51");

        Museum museum2 = new Museum();
        museum2.setId(2L);
        museum2.setTitle("Museum 2");
        museum2.setDescription("Descript");
        museum2.setAddress("Ленина, 52");

        return List.of(museum1, museum2);
    }
}