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
 * Класс для тестирования класса {@link CancelCommand}
 */
@ExtendWith(MockitoExtension.class)
class CancelCommandTest {

    @InjectMocks
    private CancelCommand cancelCommand;

    @Mock
    private EventService eventService;

    @Mock
    private UserService userService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private NotificationService notificationService;

    private CommandArgs commandArgs;

    /**
     * Настройка данных перед каждым тестом
     */
    @BeforeEach
    void setUp() {
        this.commandArgs = new CommandArgs();
        commandArgs.setChatId(1L);
        commandArgs.setCallbackData("CancelEvent 1");
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

        Message message = cancelCommand.getMessage(commandArgs);
        assertEquals("Вы отменили свою запись на выбранное мероприятие", message.getText());
        Mockito.verify(reviewService, Mockito.times(1)).deleteReview(review);
        Mockito.verify(notificationService, Mockito.times(1)).deleteNotificationEvent(user, event);
    }

    /**
     * Проверка случая,
     * когда пользователь пытается отменить запись на мероприятие,
     * на которое он не зарегистрирован
     */
    @Test
    void getMessageIfNotSignedUp() {
        Message message = cancelCommand.getMessage(commandArgs);
        assertEquals("Вы не записаны на данное мероприятие", message.getText());
        Mockito.verify(reviewService, Mockito.never()).deleteReview(Mockito.any(Review.class));
        Mockito.verify(notificationService, Mockito.never())
                .deleteNotificationEvent(Mockito.any(User.class), Mockito.any(Event.class));
    }
}