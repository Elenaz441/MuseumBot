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
import ru.urfu.museumbot.TelegramBot;
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
    TelegramBot telegramBot;

    @Mock
    ServiceContext serviceContext;

    @Mock
    EventService eventService;

    @Mock
    UserService userService;

    @Mock
    ReviewService reviewService;

    FakeSender fakeSender;

    Update update;

    /**
     * Настройка данных перед каждым тестом
     */
    @BeforeEach
    void setUp() {
        Mockito.doReturn(eventService)
                .when(serviceContext)
                .getEventService();
        Mockito.doReturn(userService)
                .when(serviceContext)
                .getUserService();
        Mockito.doReturn(reviewService)
                .when(serviceContext)
                .getReviewService();
        fakeSender = new FakeSender(telegramBot);
        this.cancelCommand = new CancelCommand(fakeSender, serviceContext);

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
    void execute() {
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

        cancelCommand.execute(update);
        assertEquals(1, fakeSender.getMessages().size());

        SendMessage messageText = fakeSender.getMessages().get(0);
        assertEquals("Вы отменили свою запись на выбранное мероприятие", messageText.getText());
        Mockito.verify(reviewService, Mockito.times(1)).deleteReview(review);
    }

    /**
     * Проверка случая,
     * когда пользователь пытается отменить запись на мероприятие,
     * на которое он не зарегистрирован
     */
    @Test
    void executeIfNotSignedUp() {
        cancelCommand.execute(update);
        SendMessage messageText = fakeSender.getMessages().get(0);
        assertEquals("Вы не записаны на данное мероприятие", messageText.getText());
        Mockito.verify(reviewService, Mockito.never()).deleteReview(Mockito.any(Review.class));
    }
}