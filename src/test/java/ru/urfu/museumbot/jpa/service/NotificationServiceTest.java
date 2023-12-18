package ru.urfu.museumbot.jpa.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.museumbot.FakeBot;
import ru.urfu.museumbot.jpa.models.Event;
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

    @BeforeEach
    void setUp() {
        bot = new FakeBot();
        notificationService = new NotificationService(userRepository,
                eventRepository,
                notificationRepository,
                bot);
        user.setChatId(1L);
        event.setTitle("Event 1");
        event.setAddress("Ленина, 51");
    }

    /**
     * Тест создания уведомления
     */
    @Test
    void createNotificationEvent() throws InterruptedException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 1);
        user.setNotificationTime(calendar.getTime());
        Calendar calendarEvent = Calendar.getInstance();
        calendarEvent.add(Calendar.DATE, 1);
        calendarEvent.set(Calendar.HOUR, 14);
        calendarEvent.set(Calendar.MINUTE, 30);
        event.setDate(calendarEvent.getTime());
        Notification notification = new Notification();
        notification.setEvent(event);
        notification.setUser(user);
        notification.setText("Завтра в 14:30 состоится мероприятие \"Event 1\" по адресу Ленина, 51");

//        Notification savedNot = notification;
//        savedNot.setId(1L);
//        Mockito.when(notificationRepository.save(notification)).thenReturn(savedNot);

        notificationService.createNotificationEvent(user, event);

        assertEquals(0, bot.getMessages().size());

        Thread.sleep(1010);

        assertEquals(1, bot.getMessages().size());
        assertEquals("Сработало напоминание: Завтра в 14:30 состоится мероприятие \"Event 1\" по адресу Ленина, 51",
                bot.getMessages().get(0).getText());
        Mockito.verify(notificationRepository, Mockito.times(1)).save(notification);
    }

//    @Test
//    void deleteNotificationEvent() throws InterruptedException {
//        Notification notification = new Notification();
//        notification.setEvent(event);
//        notification.setUser(user);
//        Notification savedNot = notification;
//        savedNot.setId(1L);
//        Mockito.when(notificationRepository.save(notification)).thenReturn(savedNot);
//        Mockito.doReturn(savedNot).when(notificationRepository).getNotificationByUserAndEvent(user, event);
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.SECOND, 1);
//        user.setNotificationTime(calendar.getTime());
//        user.setNotifications(new ArrayList<>(List.of(savedNot)));
//        Calendar calendarEvent = Calendar.getInstance();
//        calendarEvent.add(Calendar.DATE, 1);
//        event.setDate(calendarEvent.getTime());
//        event.setNotifications(new ArrayList<>(List.of(savedNot)));
//
//        notificationService.createNotificationEvent(user, event);
//        for (Map.Entry<Long, Timer> entry: notificationService.timers.entrySet()) {
//            System.out.println(entry.getKey());
//            System.out.println(entry.getValue());
//        }
//        notificationService.deleteNotificationEvent(user, event);
//
//        assertEquals(0, bot.getMessages().size());
//
//        Thread.sleep(1010);
//
//        assertEquals(0, bot.getMessages().size());
//    }

    @Test
    void deletePastNotifications() {
    }
}