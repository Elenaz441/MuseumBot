package ru.urfu.museumbot.jpa.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.Review;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.repository.UserRepository;

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
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Prov");
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
}