package ru.urfu.museumbot.JPA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.museumbot.JPA.models.Event;
import ru.urfu.museumbot.JPA.models.Review;
import ru.urfu.museumbot.JPA.models.User;


/**
 * <p>Репозиторий сущности "Отзыв"</p>
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review getReviewByUserAndEvent(User user, Event event);
}
