package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.urfu.museumbot.TelegramBot;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.service.EventService;
import ru.urfu.museumbot.jpa.service.ServiceContext;

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

    PreSignUpCommand preSignUpCommand;

    @Mock
    TelegramBot telegramBot;

    @Mock
    ServiceContext serviceContext;

    @Mock
    EventService eventService;

    FakeSender fakeSender;

    List<Event> events;

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
        Mockito.doReturn(eventService)
                .when(serviceContext)
                .getEventService();
        fakeSender = new FakeSender(telegramBot);
        this.preSignUpCommand = new PreSignUpCommand(fakeSender, serviceContext);
    }

    /**
     * Тест на вывод предстоящих мероприятий с помощью кнопок
     */
    @Test
    void execute() {
        Mockito.doReturn(events)
                .when(eventService)
                .getListEvents();
        Chat chat = new Chat(1L, "test");
        Message message = new Message();
        message.setChat(chat);
        Update update = new Update();
        update.setMessage(message);
        preSignUpCommand.execute(update);
        assertEquals(1, fakeSender.getMessages().size());

        SendMessage sendMessage = fakeSender.getMessages().get(0);
        assertEquals(
                "Выберете мероприятие, на которое хотите записаться:",
                sendMessage.getText());
        assertEquals(InlineKeyboardMarkup.class, sendMessage.getReplyMarkup().getClass());

        InlineKeyboardMarkup keyboard = (InlineKeyboardMarkup) sendMessage.getReplyMarkup();

        assertEquals(2, keyboard.getKeyboard().size());
        assertEquals(1, keyboard.getKeyboard().get(0).size());
        assertEquals("Event 1", keyboard.getKeyboard().get(0).get(0).getText());
        assertEquals("Event 2", keyboard.getKeyboard().get(1).get(0).getText());
    }
}