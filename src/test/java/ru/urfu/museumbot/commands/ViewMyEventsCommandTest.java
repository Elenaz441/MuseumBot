package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс для тестирования класса {@link ViewMyEventsCommand}
 */
@ExtendWith(MockitoExtension.class)
class ViewMyEventsCommandTest {

    @InjectMocks
    private ViewMyEventsCommand viewMyEventsCommand;

    @Mock
    private UserService userService;

    private CommandArgs commandArgs;

    private final List<Event> events;

    /**
     * Подготовка данных для тестов
     */
    public ViewMyEventsCommandTest() {
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
     * Тест на вывод предстоящих мероприятий, на которые пользователь зарегистрирован
     */
    @Test
    void getMessage() {
        Mockito.doReturn(events)
                .when(userService)
                .getUserEvents(1L);
        Message message = viewMyEventsCommand.getMessage(commandArgs);
        assertEquals("""
                        Event 1
                                        
                        Descript
                                        
                        Дата: суббота, 25 ноября 2017, 12:00
                        Длительность: 60 минут
                        Адрес: Ленина, 51
                                        
                        ==============  =================
                                        
                        Event 2
                                        
                        Descript
                                        
                        Дата: суббота, 25 ноября 2017, 12:00
                        Длительность: 60 минут
                        Адрес: Ленина, 52""",
                message.getText());
    }

    /**
     * Тест случая, когда пользователь не зарегистрирован ни на одно мероприятие
     */
    @Test
    void getMessageIfNotSignedUp() {
        Mockito.doReturn(new ArrayList<>())
                .when(userService)
                .getUserEvents(1L);
        Message message = viewMyEventsCommand.getMessage(commandArgs);
        assertEquals("Вы ещё не записаны ни на одно мероприятие", message.getText());
    }
}