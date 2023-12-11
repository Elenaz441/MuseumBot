package ru.urfu.museumbot.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.Review;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.repository.EventRepository;
import ru.urfu.museumbot.jpa.repository.ReviewRepository;
import ru.urfu.museumbot.jpa.repository.UserRepository;

import java.util.List;


/**
 * <p>Класс для работы с данными из бд для сущности "Отзыв"</p>
 */
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    /**
     * Конструктор для класса {@link ReviewService}
     * @param reviewRepository - репозиторий для сущности "Отзыв"
     * @param userRepository - репозиторий для сущности "Пользователь"
     * @param eventRepository - репозиторий для сущности "Мероприятие"
     */
    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * <p>Получить отзыв по пользователю и мероприятию</p>
     */
    public Review getReview(User user, Event event) {
        return reviewRepository.getReviewByUserAndEvent(user, event);
    }

    /**
     * <p>Удалить отзыв из базы данных</p>
     */
    @Transactional
    public void deleteReview(Review review) {
        User user = review.getUser();
        user.removeReview(review);
        userRepository.save(user);
        Event event = review.getEvent();
        event.removeReview(review);
        eventRepository.save(event);
        reviewRepository.deleteById(review.getId());
    }

    /**
     * <p>Добавить отзыв в базу данных</p>
     */
    @Transactional
    public void addReview(Review review) {
        User user = review.getUser();
        user.addReview(review);
        userRepository.save(user);
        Event event = review.getEvent();
        event.addReview(review);
        eventRepository.save(event);
        reviewRepository.save(review);
    }

    public void updateReview(Review review) {
        reviewRepository.save(review);
    }

    /**
     * Оставить оценку посещённого мероприятию
     * @param chatId чат пользователя, который оценивает
     * @param rating оценка от 0 до 10
     */
    public void rateEvent(Long chatId, int rating) {
        User user = userRepository.getUserByChatId(chatId);
        Event event = eventRepository.getEventById(user.getReviewingEvent());
        Review review = getReview(user, event);
        review.setRating(rating);
        reviewRepository.save(review);
    }
}
