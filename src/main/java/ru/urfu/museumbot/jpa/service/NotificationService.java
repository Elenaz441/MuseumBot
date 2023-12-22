package ru.urfu.museumbot.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.museumbot.Bot;
import ru.urfu.museumbot.dataFormat.EventFormat;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.Notification;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.repository.EventRepository;
import ru.urfu.museumbot.jpa.repository.NotificationRepository;
import ru.urfu.museumbot.jpa.repository.UserRepository;
import ru.urfu.museumbot.message.Message;

import java.util.*;

/**
 * <p>Класс для работы с данными из бд для сущности "Уведомление"</p>
 */
@Service
@EnableScheduling
public class NotificationService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final NotificationRepository notificationRepository;
    private final Bot bot;
    private final Map<Long, Timer> timers;

    @Autowired
    public NotificationService(UserRepository userRepository,
                               EventRepository eventRepository,
                               NotificationRepository notificationRepository,
                               @Lazy Bot bot) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.notificationRepository = notificationRepository;
        this.bot = bot;
        this.timers = new HashMap<>();
        scheduleNotificationsFromDB();
    }

    /**
     * Проверка, существует ли напоминание по данному пользователю и мероприятию
     */
    public boolean isNotificationExistedByUserAndEvent(User user, Event event) {
        return notificationRepository.existsByUserAndEvent(user, event);
    }

    /**
     * Получить уведомление по пользователю и мероприятию
     */
    public Notification getNotificationByUserAndEvent(User user, Event event) {
        return notificationRepository.getNotificationByUserAndEvent(user, event);
    }

    /**
     * Создать уведомление о мероприятии для данного пользователя
     */
    public void createNotificationEvent(User user, Event event) {
        Calendar userTimeCal = Calendar.getInstance();
        userTimeCal.setTime(user.getNotificationTime());
        Calendar sendingDateCal = Calendar.getInstance();
        sendingDateCal.setTime(event.getDate());
        sendingDateCal.add(Calendar.DATE, -1);
        sendingDateCal.set(Calendar.HOUR_OF_DAY, userTimeCal.get(Calendar.HOUR_OF_DAY));
        sendingDateCal.set(Calendar.MINUTE, userTimeCal.get(Calendar.MINUTE));
        sendingDateCal.set(Calendar.SECOND, userTimeCal.get(Calendar.SECOND));

        long duration  = sendingDateCal.getTime().getTime() - new Date().getTime();
        if (duration < 0L) {
            return;
        }

        String text = new EventFormat().toFormattedStringForNotification(event);
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setEvent(event);
        notification.setText(text);
        notification.setSendingDate(sendingDateCal.getTime());
        notificationRepository.save(notification);
        user.addNotification(notification);
        userRepository.save(user);
        event.addNotification(notification);
        eventRepository.save(event);
        schedule(notification);
    }

    /**
     * Удалить уведомление из бд
     */
    @Transactional
    public void deleteNotification(Notification notification) {
        Timer timer = timers.remove(notification.getId());
        if (timer != null) {
            timer.cancel();
        }

        User user = notification.getUser();
        user.removeNotification(notification);
        userRepository.save(user);
        Event event = notification.getEvent();
        event.removeNotification(notification);
        eventRepository.save(event);
        notificationRepository.deleteById(notification.getId());
    }

    /**
     * Поменять время уведомления по текущим настройкам пользователя
     */
    public void resetNotificationTimeForUser(Notification notification, User user) {
        Calendar userTimeCal = Calendar.getInstance();
        userTimeCal.setTime(user.getNotificationTime());

        Calendar sendingDateCal = Calendar.getInstance();
        sendingDateCal.setTime(notification.getSendingDate());
        sendingDateCal.set(Calendar.HOUR_OF_DAY, userTimeCal.get(Calendar.HOUR_OF_DAY));
        sendingDateCal.set(Calendar.MINUTE, userTimeCal.get(Calendar.MINUTE));
        long duration  = sendingDateCal.getTime().getTime() - new Date().getTime();
        if (duration < 0L) {
            return;
        }
        Timer timer = timers.remove(notification.getId());
        if (timer != null) {
            timer.cancel();
        }
        notification.setSendingDate(sendingDateCal.getTime());
        notificationRepository.save(notification);
        schedule(notification);
    }

    /**
     * Назначить таймер для данного уведомления
     */
    private void schedule(Notification notification) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message(notification.getUser().getChatId(),
                        "Напоминание! %s".formatted(notification.getText()));
                bot.sendMessage(message);
            }
        }, notification.getSendingDate());
        timers.put(notification.getId(), timer);
    }

    /**
     * Назначить таймер для уведомлений (данная функция используется во время запуска сервера)
     */
    private void scheduleNotificationsFromDB() {
        List<Notification> notifications = notificationRepository.getNotificationsBySendingDateAfter(new Date());
        for (Notification notification:notifications) {
            schedule(notification);
        }
    }

    /**
     * Удалить прошедшие уведомления
     */
    @Async
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void deletePastNotifications() {
        List<Notification> notifications = notificationRepository.getNotificationsBySendingDateBefore(new Date());
        for (Notification notification:notifications) {
            deleteNotification(notification);
        }
    }
}
