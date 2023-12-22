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
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс для тестирования класса {@link PreRateNonCommand}
 */
@ExtendWith(MockitoExtension.class)
class PreRateNonCommandTest {

    @InjectMocks
    private PreRateNonCommand preRateNonCommand;

    @Mock
    private UserService userService;

    private CommandArgs commandArgs;

    @BeforeEach
    void setUp() {
        this.commandArgs = new CommandArgs();
        commandArgs.setChatId(1L);

        User user = new User();
        user.setId(1L);
        user.setChatId(1L);
    }

    /**
     * Проверка второго этапа оценивания мероприятия
     * Когда вместе с CallbackData передаётся число - Event.id
     */
    @Test
    void getMessage() {
        commandArgs.setCallbackData("LeaveReview 1");

        Message message = preRateNonCommand.getMessage(commandArgs);

        assertEquals(
                "Как вы оцениваете данное мероприятие от 0 до 10, где 0 - это не понравилось совсем; 10 - очень понравилось?",
                message.getText());
        Mockito.verify(userService, Mockito.times(1)).updateUserState(1L, State.RATE);
        Mockito.verify(userService, Mockito.times(1)).setReviewingEvent(1L, 1L);
    }

    /**
     * Проверка второго этапа оценивания мероприятия
     * Когда CallbackData содержит некорректный Event.id
     */
    @Test
    void getMessageIfNotANumber() {
        commandArgs.setCallbackData("LeaveReview п");
        Message message = preRateNonCommand.getMessage(commandArgs);
        assertEquals(
                "Извините, не смог найти данное событие. Пожалуйста, воспользуйтесь данной функцией позже",
                message.getText());
        Mockito.verify(userService, Mockito.times(1))
                .updateUserState(1L, State.INIT);
        Mockito.verify(userService, Mockito.never())
                .setReviewingEvent(Mockito.any(Long.class), Mockito.any(Long.class));
    }
}