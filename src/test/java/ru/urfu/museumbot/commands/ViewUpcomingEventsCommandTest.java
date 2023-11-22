package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.urfu.museumbot.TelegramBot;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.repository.EventRepository;
import ru.urfu.museumbot.jpa.service.EventService;
import ru.urfu.museumbot.jpa.service.ServiceContext;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Класс для тестирования класса {@link ViewUpcomingEventsCommand}
 */
@ExtendWith(MockitoExtension.class)
class ViewUpcomingEventsCommandTest {

    @InjectMocks
    ViewUpcomingEventsCommand viewUpcomingEventsCommand;

    @Mock
    TelegramBot telegramBot;

    @Mock
    ServiceContext serviceContext;

    @Mock
    EventService eventService;

    @Mock
    EventRepository eventRepository;

    FakeSender fakeSender;

    List<Event> events;

    public  ViewUpcomingEventsCommandTest() {
        Calendar calendar = new GregorianCalendar(2017, 10 , 25, 12, 0);
        Date date = calendar.getTime();
        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle("Event 1");
        event1.setDescription("Descript");
        event1.setDate(date);
        event1.setDuration(60);
        event1.setAddress("Ленина, 51");

        Event event2 = new Event();
        event2.setId(2L);
        event2.setTitle("Event 2");
        event2.setDescription("Descript");
        event2.setDate(date);
        event2.setDuration(60);
        event2.setAddress("Ленина, 52");

        this.events = List.of(event1, event2);

        eventRepository = mock(EventRepository.class);
        eventService = new EventService(eventRepository);
        serviceContext = new ServiceContext(eventService, null, null);
    }

    @BeforeEach
    public void preProcess() {
        fakeSender = new FakeSender(telegramBot);
    }

    @Test
    void execute() {
        System.out.println(eventService);
        Mockito.doReturn(events)
                .when(eventService)
                .getListEvents();
        Chat chat = new Chat(1L, "test");
        Message message = new Message();
        message.setChat(chat);
        Update update = new Update();
        update.setMessage(message);
        viewUpcomingEventsCommand.execute(update);
        assertEquals(1, fakeSender.getMessages().size());
        assertEquals("""
                Event 1
                
                Descript
                Дата: =============================== events.get(1)""", fakeSender.getMessages().get(0));
    }
}