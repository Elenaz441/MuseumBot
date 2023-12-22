package ru.urfu.museumbot.scheduler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.config.CronTask;
import ru.urfu.museumbot.enums.DayOfWeek;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.repository.UserRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс для тестирования класса {@link CronTaskService}
 */
@ExtendWith(MockitoExtension.class)
class CronTaskServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CronTaskService cronTaskService;

    /**
     * Тестирование метода, который создаёт запланированное задание,
     * т.е. еженедельную рассылку ботом информации о случайном экспонате
     */
    @Test
    void createCronTaskRandomExhibit() throws ParseException {
        User user = new User();
        user.setId(1L);
        user.setChatId(1L);
        user.setSettingReminders(false);
        user.setRandomExposureSetting(true);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = format.parse("12:56");
        user.setNotificationTime(date);
        user.setDayOfWeekDistribution(DayOfWeek.MONDAY.ordinal());

        Mockito.doReturn(user).when(userRepository).getUserByChatId(1L);

        CronTask task = cronTaskService.createCronTaskRandomExhibit(1L);
        assertEquals("0 56 12 * * 1", task.getExpression());
    }
}