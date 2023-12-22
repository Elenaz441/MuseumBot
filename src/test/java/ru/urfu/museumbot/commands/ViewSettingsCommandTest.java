package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для тестирования класса {@link ViewSettingsCommand}
 */
@ExtendWith(MockitoExtension.class)
class ViewSettingsCommandTest {

    @InjectMocks
    private ViewSettingsCommand viewSettingsCommand;

    @Mock
    private UserService userService;

    private final User user = new User();

    private final CommandArgs commandArgs = new CommandArgs();

    /**
     * Тест на вывод информации о настройках уведомлений
     */
    @Test
    void getMessage() {
        commandArgs.setChatId(1L);
        Calendar calendar = new GregorianCalendar(2017, Calendar.NOVEMBER, 25, 12, 0);
        user.setSettingReminders(true);
        user.setRandomExposureSetting(false);
        user.setNotificationTime(calendar.getTime());
        user.setDayOfWeekDistribution(4);
        Mockito.doReturn(user)
                .when(userService)
                .getUserByChatId(1L);

        Message message = viewSettingsCommand.getMessage(commandArgs);

        assertEquals("""
                        У вас следующие настройки:
                        Присылать ли вам напоминание о мероприятии, на которое вы записаны, за день до начала? - да
                        Присылать ли вам информацию о случайном экспонате? - нет
                        Присылать уведомления в 12:00.""",
                message.getText());

        user.setSettingReminders(false);


        Message messageIfRemindersFalse = viewSettingsCommand.getMessage(commandArgs);

        assertEquals("""
                        У вас следующие настройки:
                        Присылать ли вам напоминание о мероприятии, на которое вы записаны, за день до начала? - нет
                        Присылать ли вам информацию о случайном экспонате? - нет
                        """,
                messageIfRemindersFalse.getText());

        user.setRandomExposureSetting(true);

        Message messageIfRandomExposureSettingTrue = viewSettingsCommand.getMessage(commandArgs);

        assertEquals("""
                        У вас следующие настройки:
                        Присылать ли вам напоминание о мероприятии, на которое вы записаны, за день до начала? - нет
                        Присылать ли вам информацию о случайном экспонате? - да
                        В какой день недели присылать информацию об экспонате? - Четверг
                        Присылать уведомления в 12:00.""",
                messageIfRandomExposureSettingTrue.getText());
    }
}