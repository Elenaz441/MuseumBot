package ru.urfu.museumbot.JPA.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.museumbot.JPA.models.Event;
import ru.urfu.museumbot.JPA.repository.EventRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для тестирования класса {@link EventService}
 */
@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    /**
     * Проверка получения ближайших мероприятий
     */
    @Test
    void getListEvents() {
        Date dateNow = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateNow);
        cal.add(Calendar.DATE, 3);

        Event event1 = new Event();
        event1.setTitle("event1");
        event1.setDate(cal.getTime());

        cal.add(Calendar.DATE, 7);

        Event event2 = new Event();
        event2.setTitle("event2");
        event2.setDate(cal.getTime());

        Mockito.doReturn(List.of(event1, event2))
                .when(eventRepository)
                .findAll();

        List<Event> events = eventService.getListEvents();
        assertEquals(1, events.size());
        assertEquals("event1", events.get(0).getTitle());
    }
}