package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.urfu.museumbot.TelegramBot;
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
        this.signUpCommand = new SignUpCommand(fakeSender, serviceContext);
    }

    /**
     * Тест на регистрацию пользователя
     */
    @Test
    void execute() {
        Long chatId = 1L;
        Event event = new Event();
        event.setId(1L);
        User user = new User();
        user.setId(1L);
        user.setChatId(chatId);
        Mockito.doReturn(user).when(userService).getUserByChatId(1L);
        Mockito.doReturn(event).when(eventService).getEventById(1L);

        Chat chat = new Chat(chatId, "test");
        Message message = new Message();
        message.setChat(chat);
        message.setMessageId(1);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setMessage(message);
        callbackQuery.setData("AddEvent 1");
        Update update = new Update();
        update.setCallbackQuery(callbackQuery);

        signUpCommand.execute(update);
        assertEquals(1, fakeSender.getEditedMessages().size());

        EditMessageText messageText = fakeSender.getEditedMessages().get(0);
        assertEquals("Вы записались на выбранное мероприятие", messageText.getText());
    }

    /**
     * Тест на регистрацию пользователя, если он уже зарегистрирован
     */
    @Test
    void executeIfSignedUp() {
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

        Chat chat = new Chat(chatId, "test");
        Message message = new Message();
        message.setChat(chat);
        message.setMessageId(1);
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setMessage(message);
        callbackQuery.setData("AddEvent 1");
        Update update = new Update();
        update.setCallbackQuery(callbackQuery);

        signUpCommand.execute(update);
        assertEquals(1, fakeSender.getEditedMessages().size());

        EditMessageText messageText = fakeSender.getEditedMessages().get(0);
        assertEquals("Вы уже записаны на мероприятие \"Event 1\"", messageText.getText());
    }
}