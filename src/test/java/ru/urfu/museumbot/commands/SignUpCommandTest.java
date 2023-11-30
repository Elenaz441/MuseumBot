package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.urfu.museumbot.jpa.models.*;
import ru.urfu.museumbot.jpa.service.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для тестирования класса {@link SignUpCommand}
 */
@ExtendWith(MockitoExtension.class)
class SignUpCommandTest {

    SignUpCommand signUpCommand;

    @Mock
    EventService eventService;

    @Mock
    UserService userService;

    @Mock
    ReviewService reviewService;

    Update update;

    /**
     * Настройка данных перед каждым тестом
     */
    @BeforeEach
    void setUp() {
        this.signUpCommand = new SignUpCommand(eventService, reviewService, userService);

        Chat chat = new Chat(1L, "test");
        Message message = new Message();
        message.setChat(chat);
        message.setMessageId(1);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setMessage(message);
        callbackQuery.setData("AddEvent 1");
        update = new Update();
        update.setCallbackQuery(callbackQuery);
    }

    /**
     * Тест на регистрацию пользователя
     */
    @Test
    void getMessage() {
        Long chatId = 1L;
        Event event = new Event();
        event.setId(1L);
        User user = new User();
        user.setId(1L);
        user.setChatId(chatId);
        Mockito.doReturn(user).when(userService).getUserByChatId(1L);
        Mockito.doReturn(event).when(eventService).getEventById(1L);

        SendMessage message = signUpCommand.getMessage(update);
        assertEquals("Вы записались на выбранное мероприятие", message.getText());

        Mockito.verify(reviewService, Mockito.times(1)).addReview(Mockito.any(Review.class));
    }

    /**
     * Тест на регистрацию пользователя, если он уже зарегистрирован
     */
    @Test
    void getMessageIfSignedUp() {
        Long chatId = 1L;
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Event 1");
        User user = new User();
        user.setId(1L);
        user.setChatId(chatId);
        Review review = new Review();
        review.setId(1L);
        review.setUser(user);
        review.setEvent(event);
        Mockito.doReturn(user).when(userService).getUserByChatId(1L);
        Mockito.doReturn(event).when(eventService).getEventById(1L);
        Mockito.doReturn(review).when(reviewService).getReview(user, event);
        SendMessage message = signUpCommand.getMessage(update);

        assertEquals("Вы уже записаны на мероприятие \"Event 1\"", message.getText());
    }
}