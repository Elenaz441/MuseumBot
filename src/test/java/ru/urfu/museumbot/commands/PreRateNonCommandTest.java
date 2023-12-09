package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для тестирования класса {@link PreRateNonCommand}
 */
@ExtendWith(MockitoExtension.class)
class PreRateNonCommandTest {

    @InjectMocks
    PreRateNonCommand preRateNonCommand;

    @Mock
    UserService userService;

    CommandArgs commandArgs;

    User user;

    @BeforeEach
    void setUp() {
        this.commandArgs = new CommandArgs();
        commandArgs.setChatId(1L);
        commandArgs.setCallbackData("LeaveReview 1");

        this.user = new User();
        user.setId(1L);
        user.setChatId(1L);
        user.setReviewingEvent(1L);
    }

    /**
     * Проверка второго этапа оценивания мероприятия
     */
    @Test
    void getMessage() {
        Message message = preRateNonCommand.getMessage(commandArgs);

        assertEquals(
                "Как вы оцениваете данное мероприятие от 0 до 10, где 0 - это не понравилось совсем; 10 - очень понравилось?",
                message.getText());
        Mockito.verify(userService, Mockito.times(1)).updateUserState(1L, State.RATE);
        Mockito.verify(userService, Mockito.times(1)).setReviewingEvent(1L, 1L);
    }
}