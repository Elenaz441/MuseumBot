package ru.urfu.museumbot.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.repository.EventRepository;
import java.util.*;


/**
 * <p>Класс для работы с данными из бд для сущности "Мероприятие"</p>
 */
@Service
public class EventService {

    private final EventRepository eventRepository;

    /**
     * Конструктор для класса {@link EventService}
     * @param eventRepository - репозиторий для сущности "Мероприятие"
     */
    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * <p>Получить список ближайших мероприятий (за ближайшие 7 дней)</p>
     */
    public List<Event> getListEvents() {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, 7);
        Date dateTo = cal.getTime();
        return eventRepository.findAllByDateBetweenOrderByDate(now, dateTo);
    }

    /**
     * <p>Получить мероприятие по id</p>
     */
    public Event getEventById(Long id) {
        return eventRepository.getEventById(id);
    }
}
