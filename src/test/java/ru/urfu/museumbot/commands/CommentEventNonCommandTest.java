package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.Review;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.service.EventService;
import ru.urfu.museumbot.jpa.service.ReviewService;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс для тестирования класса {@link CommentEventNonCommand}
 */
@ExtendWith(MockitoExtension.class)
class CommentEventNonCommandTest {

    @InjectMocks
    CommentEventNonCommand commentEventNonCommand;

    @Mock
    EventService eventService;

    @Mock
    ReviewService reviewService;

    @Mock
    UserService userService;

    private CommandArgs commandArgs;

    private Event event;

    private User user;

    private Review review;

    @BeforeEach
    void setUp() {
        this.commandArgs = new CommandArgs();
        commandArgs.setChatId(1L);
        commandArgs.setUserInput("Отзыв пользователя");

        this.event = new Event();
        event.setId(1L);
        event.setTitle("Event 1");

        this.user = new User();
        user.setId(1L);
        user.setChatId(1L);
        user.setReviewingEvent(1L);

        this.review = new Review();
        review.setId(1L);
        review.setUser(user);
        review.setEvent(event);
    }

    /**
     * Тест на оставление отзыва
     */
    @Test
    void getMessage() {
        Mockito.doReturn(user).when(userService).getUserByChatId(1L);
        Mockito.doReturn(event).when(eventService).getEventById(1L);
        Mockito.doReturn(review).when(reviewService).getReview(user, event);

        Message message = commentEventNonCommand.getMessage(commandArgs);

        assertEquals("Спасибо, что оставили отзыв на мероприятие 'Event 1'", message.getText());
        assertEquals("Отзыв пользователя", review.getReview());
        Mockito.verify(reviewService, Mockito.times(1)).updateReview(review);

        Mockito.verify(userService, Mockito.times(1)).updateUserState(1L, State.INIT);
    }
}