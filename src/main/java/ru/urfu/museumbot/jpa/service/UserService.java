package ru.urfu.museumbot.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.commands.State;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.Review;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.repository.UserRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>Класс для работы с данными из бд для сущности "Пользователь"</p>
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Конструктор для класса {@link EventService}
     * @param userRepository - репозиторий для сущности "Пользователь"
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
        Instant now =  Instant.now();
        User user = userRepository.getUserByChatId(chatId);
        return user.getReviews().stream()
                .filter(review -> review.getEvent().getDate().toInstant().isAfter(now))
                .map(Review::getEvent)
                .toList();
    }

    /**
     * Возвращает список мероприятий,
     * на которых пользователь находится в данный момент времени
     * @param chatId - id чата с пользователем
     * @return спсиок мероприятий которые уже начались, но пока не закончились
     */
    public List<Event> getUserEventsAfterNow(Long chatId) {
        Instant now = Instant.now();
        User user = userRepository.getUserByChatId(chatId);
        return user.getReviews().stream()
                .filter(review -> review.getEvent().getDate().toInstant().isBefore(now)
                        && review.getEvent().getDate().toInstant()
                            .plus(review.getEvent().getDuration(), ChronoUnit.MINUTES).isAfter(now))
                .map(Review::getEvent)
                .toList();
    }

    /**
     * Получить пользователя по chatId
     * @param chatId - id чата с пользователем
     */
    public User getUserByChatId(Long chatId) {
        return userRepository.getUserByChatId(chatId);
    }

    /**
     * Получить список мероприятий пользователя по идентификатору чата
     * @param chatId идентификатор чата пользователя
     */
    public List<Event> getAllVisitedEvents(Long chatId) {
        Instant now = Instant.now();
        return userRepository.getUserByChatId(chatId)
                .getReviews().stream()
                .filter(review -> review.getEvent().getDate().
                        toInstant().isBefore(now))
                .map(Review::getEvent)
                .collect(Collectors.toList());
    }

    /**
     * Устанавливает состояние чата пользователя
     * @param chatId Идентификатор чата пользователя
     * @param state устанавливаемое состояние
     */
    public void updateUserState(Long chatId, State state) {
        User user = getUserByChatId(chatId);
        user.setState(state.getStateString());
        userRepository.save(user);
    }

    /**
     * Устанавливает идентификатор мероприятия, на которое пользователь оставляет отзыв в данный момент
     * @param chatId чат пользователя
     * @param eventId - id мероприятия
     */
    public void setReviewingEvent(Long chatId, Long eventId){
        User user = getUserByChatId(chatId);
        user.setReviewingEvent(eventId);
        userRepository.save(user);
    }
}
