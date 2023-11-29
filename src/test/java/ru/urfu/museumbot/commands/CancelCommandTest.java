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
 * Класс для тестирования класса {@link CancelCommand}
 */
@ExtendWith(MockitoExtension.class)
class CancelCommandTest {

    CancelCommand cancelCommand;

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
        this.cancelCommand = new CancelCommand(eventService, reviewService, userService);

        Chat chat = new Chat(1L, "test");
        Message message = new Message();
        message.setChat(chat);
        message.setMessageId(1);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setMessage(message);
        callbackQuery.setData("CancelEvent 1");
        update = new Update();
        update.setCallbackQuery(callbackQuery);
    }

    /**
     * Тест на отмену регистрации пользователя
     */
    @Test
    void getMessage() {
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

        SendMessage message = cancelCommand.getMessage(update);
        assertEquals("Вы отменили свою запись на выбранное мероприятие", message.getText());
        Mockito.verify(reviewService, Mockito.times(1)).deleteReview(review);
    }

    /**
     * Проверка случая,
     * когда пользователь пытается отменить запись на мероприятие,
     * на которое он не зарегистрирован
     */
    @Test
    void getMessageIfNotSignedUp() {
        SendMessage message = cancelCommand.getMessage(update);
        assertEquals("Вы не записаны на данное мероприятие", message.getText());
        Mockito.verify(reviewService, Mockito.never()).deleteReview(Mockito.any(Review.class));
    }
}