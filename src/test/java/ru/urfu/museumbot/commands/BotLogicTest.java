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
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.Review;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.service.EventService;
import ru.urfu.museumbot.jpa.service.ReviewService;
import ru.urfu.museumbot.jpa.service.UserService;

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

    List<Event> events;

    /**
     * Конструктор класса, здесь создается список мероприятий
     */
    public BotLogicTest() {
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

        this.events = List.of(event1, event2);
    }

    /**
     * Тест для функции /view_upcoming_events
     */
    @Test
    void handleIncomingTextMessage_ViewUpcomingEvents() {
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
        assertEquals("Event 2", keyboard.getKeyboard().get(1).get(0).getText());
    }

    /**
     * Тест для функции /cancel
     */
    @Test
    void handleIncomingTextMessage_cancel() {
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
     * Тест на регистрацию пользователя на выбранное мероприятие
     */
    @Test
    void handleCallbackQuery_addEvent() {
        Event event = new Event();
        User user = new User();
        Mockito.doReturn(user).when(userService).getUserByChatId(1L);
        Mockito.doReturn(event).when(eventService).getEventById(1L);

        EditMessageText messageText = logic.handleCallbackQuery("AddEvent1", 111, 1L);
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

        EditMessageText messageText = logic.handleCallbackQuery("AddEvent1", 111, 1L);
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

        EditMessageText messageText = logic.handleCallbackQuery("CancelEvent1", 111, 1L);
        assertEquals("Вы отменили свою запись на выбранное мероприятие", messageText.getText());
    }
}