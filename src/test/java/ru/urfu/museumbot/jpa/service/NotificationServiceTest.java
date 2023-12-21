package ru.urfu.museumbot.jpa.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.museumbot.FakeBot;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.Museum;
import ru.urfu.museumbot.jpa.models.Notification;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.repository.EventRepository;
import ru.urfu.museumbot.jpa.repository.NotificationRepository;
import ru.urfu.museumbot.jpa.repository.UserRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для тестирования класса {@link NotificationService}
 */
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    private NotificationService notificationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private NotificationRepository notificationRepository;

    private FakeBot bot;
    private final User user = new User();
    private final Event event = new Event();
    private final Notification notification = new Notification();

    /**
     * Настройка данных перед каждым тестом
     */
    @BeforeEach
    void setUp() {
        bot = new FakeBot();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 1);
        user.setNotificationTime(calendar.getTime());
        user.setChatId(1L);

        event.setTitle("Event 1");
        event.setAddress("Ленина, 51");
        Calendar calendarEvent = Calendar.getInstance();
        calendarEvent.add(Calendar.DATE, 1);
        calendarEvent.set(Calendar.HOUR_OF_DAY, 14);
        calendarEvent.set(Calendar.MINUTE, 30);
        event.setDate(calendarEvent.getTime());

        notification.setEvent(event);
        notification.setUser(user);
    }

    /**
     * Тест создания уведомления
     */
    @Test
    void createNotificationEvent() throws InterruptedException {
        Museum museum = new Museum();
        museum.setTitle("Museum 1");
        event.setMuseum(museum);
        notificationService = new NotificationService(userRepository,
                eventRepository,
                notificationRepository,
                bot);
        notification.setText("Мероприятие \"Event 1\" состоится завтра в 14:30 по адресу Ленина, 51 (Museum 1).");

        notificationService.createNotificationEvent(user, event);

        assertEquals(0, bot.getMessages().size());

        Thread.sleep(1010);

        assertEquals(1, bot.getMessages().size());
        assertEquals("Напоминание! Мероприятие \"Event 1\" состоится завтра в 14:30 по адресу Ленина, 51 (Museum 1).",
                bot.getMessages().get(0).getText());
        Mockito.verify(notificationRepository, Mockito.times(1)).save(notification);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(eventRepository, Mockito.times(1)).save(event);
    }

    /**
     * Тестирование назначения уведомлений из бд при запуске приложения
     */
    @Test
    void scheduleNotificationsFromDB() throws InterruptedException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 1);
        notification.setSendingDate(calendar.getTime());
        notification.setId(1L);
        Mockito.doReturn(List.of(notification))
                .when(notificationRepository)
                .getNotificationsBySendingDateAfter(Mockito.any(Date.class));
        notificationService = new NotificationService(userRepository,
                eventRepository,
                notificationRepository,
                bot);

        assertEquals(0, bot.getMessages().size());

        Thread.sleep(1010);

        assertEquals(1, bot.getMessages().size());
    }

    /**
     * Тестирование удаления уведомления
     */
    @Test
    void deleteNotification() throws InterruptedException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 1);
        notification.setSendingDate(calendar.getTime());
        notification.setId(1L);
        Mockito.doReturn(List.of(notification))
                .when(notificationRepository)
                .getNotificationsBySendingDateAfter(Mockito.any(Date.class));
        user.setNotifications(new ArrayList<>(List.of(notification)));
        event.setNotifications(new ArrayList<>(List.of(notification)));
        notificationService = new NotificationService(userRepository,
                eventRepository,
                notificationRepository,
                bot);

        notificationService.deleteNotification(notification);

        Thread.sleep(1010);

        assertEquals(0, bot.getMessages().size());
        assertEquals(0, user.getNotifications().size());
        assertEquals(0, event.getNotifications().size());
        Mockito.verify(notificationRepository, Mockito.times(1))
                .deleteById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(eventRepository, Mockito.times(1)).save(event);
    }

    /**
     * Тестирование перезаписывания времени отправки при настройке уведомлений
     */
    @Test
    void resetNotificationTimeForUser() {
        notificationService = new NotificationService(userRepository,
                eventRepository,
                notificationRepository,
                bot);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        user.setNotificationTime(calendar.getTime());

        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 15);
        notification.setSendingDate(calendar.getTime());

        notificationService.resetNotificationTimeForUser(notification, user);

        calendar.setTime(notification.getSendingDate());

        assertEquals(12, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        Mockito.verify(notificationRepository, Mockito.times(1)).save(notification);
    }

    /**
     * Тестирование удаления прошедших мероприятий
     */
    @Test
    void deletePastNotifications() {
        notification.setId(1L);
        Mockito.doReturn(List.of(notification))
                .when(notificationRepository)
                .getNotificationsBySendingDateBefore(Mockito.any(Date.class));
        user.setNotifications(new ArrayList<>(List.of(notification)));
        event.setNotifications(new ArrayList<>(List.of(notification)));
        notificationService = new NotificationService(userRepository,
                eventRepository,
                notificationRepository,
                bot);

        notificationService.deletePastNotifications();

        assertEquals(0, user.getNotifications().size());
        assertEquals(0, event.getNotifications().size());
        Mockito.verify(notificationRepository, Mockito.times(1))
                .deleteById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(eventRepository, Mockito.times(1)).save(event);
    }
}