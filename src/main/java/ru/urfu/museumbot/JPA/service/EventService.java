package ru.urfu.museumbot.JPA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.JPA.models.Event;
import ru.urfu.museumbot.JPA.repository.EventRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import java.util.*;


/**
 * <p>Класс для работы с данными из бд для сущности "Мероприятие"</p>
 */
@Service
public class EventService {

    public static final int AMOUNT_TO_ADD_DATE = 7;

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
     * <p>Получить список ближайших мероприятий</p>
     */
    public List<Event> getListEvents() {
        Instant now = new Date().toInstant();
        Instant dateTo = now.plus(AMOUNT_TO_ADD_DATE, ChronoUnit.DAYS);
        return eventRepository.findAll().stream().filter(event -> event.getDate().
                        toInstant().isBefore(dateTo) && event.getDate().toInstant().isAfter(now))
                .sorted(Comparator.comparing(Event::getDate))
                .collect(Collectors.toList());
    }

    /**
     * <p>Получить мероприятие по id</p>
     */
    public Event getEventById(Long id) {
        return eventRepository.getEventById(id);
    }
}
