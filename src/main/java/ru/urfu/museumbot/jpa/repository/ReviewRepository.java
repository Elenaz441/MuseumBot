package ru.urfu.museumbot.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.Review;
import ru.urfu.museumbot.jpa.models.User;

import java.util.List;


/**
 * <p>Репозиторий сущности "Отзыв"</p>
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review getReviewByUserAndEvent(User user, Event event);
    @Query(value = "SELECT * FROM review WHERE user_id = :user", nativeQuery = true)
    List<Review> getAllByUser(Long user);
}
