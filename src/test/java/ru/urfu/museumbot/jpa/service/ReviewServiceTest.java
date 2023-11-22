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
import ru.urfu.museumbot.jpa.repository.EventRepository;
import ru.urfu.museumbot.jpa.repository.ReviewRepository;
import ru.urfu.museumbot.jpa.repository.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Класс для тестирования класса {@link ReviewService}
 */
@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    ReviewService reviewService;

    @Mock
    UserRepository userRepository;

    @Mock
    EventRepository eventRepository;

    @Mock
    ReviewRepository reviewRepository;

    /**
     * Проверка удаления Отзыва
     */
    @Test
    void deleteReview() {
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Prov");
        event.setDate(new Date());

        User user = new User();
        user.setId(2L);
        user.setChatId(3L);

        Review review = new Review();
        review.setUser(user);
        review.setEvent(event);
        List<Review> reviews = new ArrayList<>();
        reviews.add(review);

        user.setReviews(reviews);
        event.setReviews(reviews);

        reviewService.deleteReview(review);

        assertEquals(0, user.getReviews().size());
        assertEquals(0, event.getReviews().size());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(eventRepository, Mockito.times(1)).save(event);
    }
}