package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.museumbot.enums.State;
import ru.urfu.museumbot.jpa.models.Notification;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.service.NotificationService;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс для тестирования класса {@link SetSettingNotificationNonCommand}
 */
@ExtendWith(MockitoExtension.class)
class SetSettingNotificationNonCommandTest {

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private SetSettingNotificationNonCommand settingNotificationNonCommand;

    private final CommandArgs args;

    public SetSettingNotificationNonCommandTest() {
        args = new CommandArgs();
        args.setChatId(1L);
    }

    /**
     * Тестирование команды, когда пользовательский ввод не соответствует формату
     */
    @Test
    void getMessageIfUserInputIncorrect() {
        args.setUserInput("123");
        Message message = settingNotificationNonCommand.getMessage(args);
        Mockito.verify(notificationService, Mockito.never()).deleteNotification(Mockito.any(Notification.class));
        Mockito.verify(userService, Mockito.never()).updateUserState(Mockito.anyLong(), Mockito.any(State.class));
        Mockito.verify(userService, Mockito.never()).updateNotificationSettings(Mockito.anyLong(), Mockito.anyBoolean());
        assertEquals("Настройка не выбрана. Чтобы установить настройку напишите," +
                " пожалуйста, да или нет.", message.getText());
    }

    /**
     * Тестирование команды, когда пользовательский ввод соответствует формату
     */
    @Test
    void getMessageIfUserInputCorrect() {
        args.setUserInput("да");
        Message message = settingNotificationNonCommand.getMessage(args);
        Mockito.verify(notificationService, Mockito.never()).deleteNotification(Mockito.any(Notification.class));
        Mockito.verify(userService, Mockito.times(1)).updateNotificationSettings(1L, true);
        Mockito.verify(userService, Mockito.times(1)).updateUserState(1L, State.SET_DISTRIBUTION);
        assertEquals("Присылать ли вам информацию о случайном экспонате? " +
                "(Напишите да или нет)", message.getText());
    }

    /**
     * Проверка, что удаляются напоминания о мероприятиях, если пользователь ввел "Нет"
     */
    @Test
    void getMessageIfUserInputNo() {
        args.setUserInput("нет");
        User user = new User();
        user.setId(1L);
        user.setChatId(1L);
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setUser(user);
        user.setNotifications(List.of(notification));
        Mockito.doReturn(user).when(userService).getUserByChatId(1L);

        settingNotificationNonCommand.getMessage(args);

        Mockito.verify(notificationService, Mockito.times(1)).deleteNotification(notification);
    }
}