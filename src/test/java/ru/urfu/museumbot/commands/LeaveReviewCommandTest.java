package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для тестирования класса {@link LeaveReviewCommand}
 */
@ExtendWith(MockitoExtension.class)
class LeaveReviewCommandTest {

    @InjectMocks
    LeaveReviewCommand leaveReviewCommand;

    @Mock
    UserService userService;

    CommandArgs commandArgs;

    User user;

    List<Event> events;

    public LeaveReviewCommandTest() {
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

    @BeforeEach
    void setUp() {
        this.commandArgs = new CommandArgs();
        commandArgs.setChatId(1L);

        this.user = new User();
        user.setId(1L);
        user.setChatId(1L);
        user.setReviewingEvent(1L);
    }

    /**
     * Тест на начало команды /leave_review
     */
    @Test
    void getMessage() {
        Mockito.doReturn(events).when(userService).getAllVisitedEvents(1L);

        Message message = leaveReviewCommand.getMessage(commandArgs);

        assertEquals("Выберите мероприятие:\n", message.getText());
        assertTrue(message.getButtonsContext().isPresent());
        assertEquals(2, message.getButtonsContext().get().getVariants().size());
        assertEquals("LeaveReview", message.getButtonsContext().get().getCallbackData());
        assertEquals("Event 1", message.getButtonsContext().get().getVariants().get(1L));
        assertEquals("Event 2", message.getButtonsContext().get().getVariants().get(2L));
        Mockito.verify(userService, Mockito.times(1)).updateUserState(1L, State.RATE_PREV);
    }
}