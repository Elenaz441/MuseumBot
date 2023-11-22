package ru.urfu.museumbot.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.museumbot.jpa.models.Event;

import java.util.Date;
import java.util.List;


/**
 * <p>Репозиторий сущности "Мероприятие"</p>
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Event getEventById(Long id);

    List<Event> findAllByDateBetweenOrderByDate(Date startDate, Date endDate);
}
