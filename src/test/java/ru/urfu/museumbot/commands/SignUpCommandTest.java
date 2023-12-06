package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.museumbot.jpa.models.*;
import ru.urfu.museumbot.jpa.service.*;
import ru.urfu.museumbot.message.Message;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для тестирования класса {@link SignUpCommand}
 */
@ExtendWith(MockitoExtension.class)
class SignUpCommandTest {

    @InjectMocks
    SignUpCommand signUpCommand;

    @Mock
    EventService eventService;

    @Mock
    UserService userService;

    @Mock
    ReviewService reviewService;

    CommandArgs commandArgs;

    /**
     * Настройка данных перед каждым тестом
     */
    @BeforeEach
    void setUp() {
        this.commandArgs = new CommandArgs();
        commandArgs.setChatId(1L);
        commandArgs.setCallbackData("AddEvent 1");
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

        Message message = signUpCommand.getMessage(commandArgs);
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
        Message message = signUpCommand.getMessage(commandArgs);

        assertEquals("Вы уже записаны на мероприятие \"Event 1\"", message.getText());
    }
}