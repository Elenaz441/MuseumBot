package ru.urfu.museumbot.JPA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.museumbot.JPA.models.Event;
import ru.urfu.museumbot.JPA.models.Review;
import ru.urfu.museumbot.JPA.models.User;
import ru.urfu.museumbot.JPA.repository.EventRepository;
import ru.urfu.museumbot.JPA.repository.ReviewRepository;
import ru.urfu.museumbot.JPA.repository.UserRepository;


/**
 * <p>Класс для работы с данными из бд для сущности "Отзыв"</p>
 */
@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;

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
}
