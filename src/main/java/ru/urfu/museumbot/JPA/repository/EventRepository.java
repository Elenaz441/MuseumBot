package ru.urfu.museumbot.JPA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.museumbot.JPA.models.Event;


/**
 * <p>Репозиторий сущности "Мероприятие"</p>
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Event getEventById(Long id);
}
