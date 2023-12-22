package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.museumbot.enums.State;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.service.ReviewService;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс для тестирования класса {@link RateEventNonCommand}
 */
@ExtendWith(MockitoExtension.class)
class RateEventNonCommandTest {

    @InjectMocks
    private RateEventNonCommand rateEventNonCommand;

    @Mock
    private UserService userService;

    @Mock
    private ReviewService reviewService;

    private CommandArgs commandArgs;

    @BeforeEach
    void setUp() {
        this.commandArgs = new CommandArgs();
        commandArgs.setChatId(1L);

        User user = new User();
        user.setId(1L);
        user.setChatId(1L);
        user.setReviewingEvent(1L);
    }

    /**
     * Простая проверка, когда пользователь вводит корректные данные
     */
    @Test
    void getMessage() {
        commandArgs.setUserInput("10");

        Message message = rateEventNonCommand.getMessage(commandArgs);

        assertEquals(
                "Напишите, пожалуйста, небольшой отзыв. Что вам понравилось/запомнилось больше всего.",
                message.getText());

        Mockito.verify(userService, Mockito.times(1)).updateUserState(1L, State.COMMENT);
        Mockito.verify(reviewService, Mockito.times(1)).rateEvent(1L, 10);
    }

    /**
     * Проверка, когда пользователь вводит некорректные данные
     */
    @Test
    void getMessageIfUserInputNotCorrect() {
        commandArgs.setUserInput("-10");

        Message message = rateEventNonCommand.getMessage(commandArgs);

        assertEquals("Пожалуйста, введите целое число от 0 до 10", message.getText());
        Mockito.verify(userService, Mockito.times(1)).updateUserState(1L, State.RATE);
        Mockito.verify(reviewService, Mockito.never()).rateEvent(Mockito.any(Long.class), Mockito.any(Integer.class));
    }
}