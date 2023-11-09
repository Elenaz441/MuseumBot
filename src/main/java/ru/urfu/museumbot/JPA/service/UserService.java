package ru.urfu.museumbot.JPA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.JPA.models.Event;
import ru.urfu.museumbot.JPA.models.Review;
import ru.urfu.museumbot.JPA.models.User;
import ru.urfu.museumbot.JPA.repository.UserRepository;

import java.util.List;


/**
 * <p>Класс для работы с данными из бд для сущности "Пользователь"</p>
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Добавляет нового пользователя в базу данных
     * @param newUser - новый пользователь
     */
    public void addUser(User newUser) {
        if (!userRepository.existsByChatId(newUser.getChatId()))
            userRepository.save(newUser);
    }

    /**
     * Возвращает список мероприятий на которые записался пользователь
     * @param chatId - id чата с пользователем
     */
    public List<Event> getUserEvents(Long chatId) {
        User user = userRepository.getUserByChatId(chatId);
        return user.getReviews().stream().map(Review::getEvent).toList();
    }

    /**
     * Получить пользователя по chatId
     * @param chatId - id чата с пользователем
     */
    public User getUserByChatId(Long chatId) {
        return userRepository.getUserByChatId(chatId);
    }
}
