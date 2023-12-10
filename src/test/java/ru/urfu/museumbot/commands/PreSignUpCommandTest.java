package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.service.EventService;
import ru.urfu.museumbot.message.Message;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для тестирования класса {@link PreSignUpCommand}
 */
@ExtendWith(MockitoExtension.class)
class PreSignUpCommandTest {

    @InjectMocks
    PreSignUpCommand preSignUpCommand;

    @Mock
    EventService eventService;

    List<Event> events;

    CommandArgs commandArgs;

    /**
     * Подготовка данных для тестов
     */
    public PreSignUpCommandTest() {
        Calendar calendar = new GregorianCalendar(2017, Calendar.NOVEMBER, 25, 12, 0);
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
    }

    /**
     * Настройка данных перед каждым тестом
     */
    @BeforeEach
    void setUp() {
        this.commandArgs = new CommandArgs();
        commandArgs.setChatId(1L);
    }

    /**
     * Тест на вывод предстоящих мероприятий с помощью кнопок
     */
    @Test
    void getMessage() {
        Mockito.doReturn(events)
                .when(eventService)
                .getListEvents();
        Message message = preSignUpCommand.getMessage(commandArgs);
        assertEquals(
                "Выберете мероприятие, на которое хотите записаться:",
                message.getText());
        assertTrue(message.getButtonsContext().isPresent());
        assertEquals(2, message.getButtonsContext().get().getVariants().size());
        assertEquals("AddEvent", message.getButtonsContext().get().getCallbackData());
        assertEquals("Event 1", message.getButtonsContext().get().getVariants().get(0).getTitle());
        assertEquals("Event 2", message.getButtonsContext().get().getVariants().get(1).getTitle());
    }
}