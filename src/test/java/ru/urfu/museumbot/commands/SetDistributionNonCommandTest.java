package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.museumbot.enums.State;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.service.SchedulerService;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

import static org.junit.jupiter.api.Assertions.*;

 /**
 * Класс для тестирования класса {@link SetDistributionNonCommand}
 */
@ExtendWith(MockitoExtension.class)
class SetDistributionNonCommandTest {
    @Mock
    private UserService userService;
    @Mock
    private SchedulerService schedulerService;

    @InjectMocks
    private SetDistributionNonCommand setDistributionNonCommand;
    private final CommandArgs args;

    public SetDistributionNonCommandTest() {
        args = new CommandArgs();
        args.setChatId(1L);
    }

    /**
     * Тестирование команды, в случае некорректного пользовательского ввода
     */
    @Test
    void getMessageIfIncorrectUserInput(){
        args.setUserInput("1");
        Message message = setDistributionNonCommand.getMessage(args);
        assertEquals("Настройка не выбрана. Чтобы установить настройку напишите," +
                " пожалуйста, да или нет.", message.getText());
        Mockito.verify(userService, Mockito.never()).updateUserState(Mockito.any(Long.class), Mockito.any(State.class));
    }

    /**
     * Тестирование команды, когда пользовательский ввод корректен и положителен
     */
    @Test
    void getMessageIfCorrectUserInputYes() {
        args.setUserInput("Да");
        Message message = setDistributionNonCommand.getMessage(args);
          Mockito.verify(userService, Mockito.times(1))
                .updateRandomExposureSetting(1L, true);
        Mockito.verify(userService, Mockito.times(1))
                .updateUserState(1L, State.SET_DAY_OF_WEEK);
        assertEquals("Напишите в какой день недели вы хотели бы получать информацию о случайном экспонате." +
                " Например, Пятница.", message.getText());
    }

    /**
     * Тестирвоание команды, когда пользвоатель ответил положительно хотя бы на одну из настроек
     */
    @Test
    void getMessageIfCorrectUserInputNoRemindersYes() {
        args.setUserInput("Нет");
        User user = new User();
        user.setId(1L);
        user.setChatId(1L);
        user.setSettingReminders(true);
        Mockito.doReturn(user).when(userService).getUserByChatId(1L);
        Message message = setDistributionNonCommand.getMessage(args);
        Mockito.verify(schedulerService, Mockito.times(1)).removeCron(1L);
        Mockito.verify(userService, Mockito.times(1))
                .updateRandomExposureSetting(1L, false);
        Mockito.verify(userService, Mockito.times(1))
                .updateUserState(1L, State.SET_TIME);
        assertEquals("Когда вам присылать уведомления?" +
                " (Напишите конкретное время, например 12:00)", message.getText());
    }
    /**
     * Тестирвоание команды, когда пользвоатель выбрал не посылать напоминания и рассылку
     */
    @Test
    void getMessageIfCorrectUserInputNoRemindersNo() {
        args.setUserInput("Нет");
        User user = new User();
        user.setId(1L);
        user.setChatId(1L);
        user.setSettingReminders(false);
        Mockito.doReturn(user).when(userService).getUserByChatId(1L);
        Message message = setDistributionNonCommand.getMessage(args);
        Mockito.verify(schedulerService, Mockito.times(1)).removeCron(1L);
        Mockito.verify(userService, Mockito.times(1)).updateUserState(1L, State.INIT);
        assertEquals("Настройки успешно заданы.", message.getText());
    }
}