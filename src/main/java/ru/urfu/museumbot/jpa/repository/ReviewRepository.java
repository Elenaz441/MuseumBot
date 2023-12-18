package ru.urfu.museumbot.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.Review;
import ru.urfu.museumbot.jpa.models.User;

/**
 * <p>Репозиторий сущности "Отзыв"</p>
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * @param user пользователь
     * @param event событие
     * @return отзыв по пользвоателю и событию
     */
    Review getReviewByUserAndEvent(User user, Event event);
}
