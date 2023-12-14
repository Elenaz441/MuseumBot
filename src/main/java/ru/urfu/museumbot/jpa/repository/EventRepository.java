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

    /**
     * @param id идентификатор мероприятия
     * @return мепориятие по идентификатору
     */
    Event getEventById(Long id);

    /**
     * получить список мероприятий между датами
     * @param startDate с какой даты включать мероприятия
     * @param endDate до какой даты включать мероприятия
     */
    List<Event> findAllByDateBetweenOrderByDate(Date startDate, Date endDate);
}
