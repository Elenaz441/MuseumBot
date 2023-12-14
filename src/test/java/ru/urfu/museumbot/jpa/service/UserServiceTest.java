package ru.urfu.museumbot.jpa.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.museumbot.jpa.models.*;
import ru.urfu.museumbot.jpa.repository.UserRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Класс для тестирования класса {@link UserService}
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    /**
     * Проверка на добавление пользователя, если не существует пользователя с таким chatId
     */
    @Test
    void addUserIfNotExist() {
        User user = new User();
        user.setId(1L);
        user.setChatId(111L);
        user.setName("AAA");

        userService.addUser(user);

        assertEquals(1L, user.getId());
        assertEquals(111L, user.getChatId());
        assertEquals("AAA", user.getName());
        assertTrue(user.isSettingReminders());
        assertFalse(user.isRandomExposureSetting());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    /**
     * Проверка на добавление пользователя, если существует пользователь с таким chatId
     */
    @Test
    void addUserIfExist() {
        User user = new User();
        user.setChatId(112L);

        Mockito.doReturn(true)
                .when(userRepository)
                .existsByChatId(112L);

        userService.addUser(user);

        Mockito.verify(userRepository, Mockito.never()).save(user);
    }

    /**
     * Проверка на получение списка событий, на которые пользователь зарегистрирован
     */
    @Test
    void getUserEvents() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();

        Event event = new Event();
        event.setId(1L);
        event.setTitle("Prov");
        event.setDate(dt);
        User user = new User();
        user.setId(2L);
        user.setChatId(3L);
        Review review = new Review();
        review.setUser(user);
        review.setEvent(event);
        user.setReviews(List.of(review));

        Mockito.doReturn(user)
                .when(userRepository)
                .getUserByChatId(3L);

        List<Event> events = userService.getUserEvents(3L);

        assertEquals(1, events.size());
        assertEquals(1L, events.get(0).getId());
        assertEquals("Prov", events.get(0).getTitle());

    }

    /**
     * Тестирвоание метода, который возвращает
     * мероприятия на которых пользователь находится в данный момент времени
     * (Вытсавка происходит сейчас)
     */
    @Test
    void getUserEventsAfterNow(){
        User user = new User();
        user.setId(1L);
        user.setChatId(1L);

        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle("Event 1");
        event1.setDate(Date.from(Instant.now().minus(15, ChronoUnit.MINUTES)));
        event1.setDuration(60);

        Calendar calendar1 = new GregorianCalendar(2023, Calendar.DECEMBER, 9, 12, 0);
        Date date1 = calendar1.getTime();
        Event event2 = new Event();
        event2.setId(2L);
        event2.setTitle("Event 2");
        event2.setDate(date1);
        event2.setDuration(60);

        Review review = new Review();
        review.setEvent(event1);
        review.setUser(user);
        review.setId(1L);

        Review review1 = new Review();
        review1.setEvent(event2);
        review1.setUser(user);
        review1.setId(2L);

        user.setReviews(List.of(review, review1));
        Mockito.when(userRepository.getUserByChatId(1L)).thenReturn(user);
        List<Event> res = userService.getUserEventsAfterNow(1L);
        Assertions.assertEquals(1L, res.get(0).getId());
        Instant now =  Instant.now();
        Assertions.assertEquals(1, res.size());
        Assertions.assertTrue(res.get(0).getDate().toInstant().isBefore(now));
        Assertions.assertTrue(res.get(0).getDate().toInstant().plus(res.get(0).getDuration(), ChronoUnit.MINUTES).isAfter(now));
    }
}