package ru.urfu.museumbot.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.Bot;
import ru.urfu.museumbot.dataFormat.CronExpressionFormat;
import ru.urfu.museumbot.dataFormat.ExhibitFormat;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.repository.UserRepository;
import ru.urfu.museumbot.jpa.service.ExhibitService;
import ru.urfu.museumbot.message.Message;

import java.util.*;

/**
 * Класс для планирования рассылки каждому пользователю индивидуально в зависимости от настроек
 * @see org.springframework.scheduling.config.CronTask
 * инструмент для реализиции {@link Bot} функции рассылки информации
 */
@Service
public class CronTaskService {
    private final Bot bot;
    private final UserRepository userRepository;
    private final ExhibitService exhibitService;
    private final LinkedHashMap<Long, CronTask> cronTasks;

    @Autowired
    public CronTaskService(UserRepository userRepository, ExhibitService exhibitService, @Lazy Bot bot) {
        this.bot = bot;
        this.userRepository = userRepository;
        this.cronTasks = createCronTaskFromDB();
        this.exhibitService = exhibitService;
    }


    public LinkedHashMap<Long, CronTask> getCronTasks() {
        return cronTasks;
    }

    /**
     * @return Словарь, который содержит все отложенные задания из дазы данных для рассылки
     */
    private LinkedHashMap<Long, CronTask> createCronTaskFromDB() {
        LinkedHashMap<Long, CronTask> tasks = new LinkedHashMap<>();
        List<User> subscriberes = userRepository.getUserByRandomExposureSetting(true);
        for (User user : subscriberes) {
            CronTask task = createCronTaskRandomExhibit(user.getChatId());
            tasks.put(user.getChatId(), task);
        }
        return tasks;
    }

    /**
     * создаёт запланированное задание
     */
    public CronTask createCronTask(Runnable task, String expression) {
        return new CronTask(task, new CronTrigger(expression));
    }

    /**
     * @return запланированное задание, т.е. еженедельную рассылку ботом информации о рандомном экспонате
     */
    public CronTask createCronTaskRandomExhibit(Long chatId) {
        User user = userRepository.getUserByChatId(chatId);
        Date notificationTime = user.getNotificationTime();
        int dayOfWeek = user.getDayOfWeekDistribution();
        String expression = new CronExpressionFormat().getCronExpressionByUserSettings(notificationTime, dayOfWeek);
        System.out.println(expression);
        System.out.println(user.getChatId());
        return createCronTask(() -> bot.sendMessage(new Message(user.getChatId(),
                new ExhibitFormat().toFormattedStringForDistribution(exhibitService.getRandomExhibit()))), expression);
    }
}