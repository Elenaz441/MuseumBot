package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.museumbot.jpa.models.*;
import ru.urfu.museumbot.jpa.service.ExhibitService;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PreViewExhibitCommandTest {
    private final Event event1;

    private CommandArgs commandArgs;
    @InjectMocks
    PreViewExhibitCommand preViewExhibitCommand;
    @Mock
    ExhibitService exhibitService;
    @Mock
    UserService userService;
    private final List<Exhibit> exhibits;

    public PreViewExhibitCommandTest() {
        Museum museum = new Museum();
        museum.setId(1L);
        museum.setAddress("Ленина, 51");
        museum.setTitle("музей1");

        event1 = new Event();
        event1.setId(1L);
        event1.setTitle("Event 1");
        event1.setDescription("Descript");
        event1.setDate(Date.from(Instant.now().minus(15, ChronoUnit.MINUTES)));
        event1.setDuration(60);
        event1.setAddress("Ленина, 51");
        event1.setMuseum(museum);

        Exhibit exhibit1 = new Exhibit();
        exhibit1.setId(1L);
        exhibit1.setMuseum(museum);
        exhibit1.setTitle("Экспонат 1");
        exhibit1.setDescription("Описание 1");

        Exhibit exhibit2 = new Exhibit();
        exhibit2.setId(2L);
        exhibit2.setMuseum(museum);
        exhibit2.setTitle("Экспонат 2");
        exhibit2.setDescription("Описание 2");

        this.exhibits = List.of(exhibit1, exhibit2);

        museum.setExhibits(exhibits);
    }
    @BeforeEach
    void setUp(){
        this.commandArgs = new CommandArgs();
        commandArgs.setChatId(1L);
    }

    /**
     * Если пользователь сейчас на выставке, то он может посмотреть экспонаты
     */
    @Test
    void getMessage(){
        Mockito.when(userService.getUserEventsAfterNow(1L)).thenReturn(List.of(event1));
        Mockito.when(exhibitService.getMuseumExhibits(1L)).thenReturn(exhibits);
        Message message = preViewExhibitCommand.getMessage(commandArgs);
        Assertions.assertEquals("Выберите экспонат:", message.getText());
        Assertions.assertTrue(message.getButtonsContext().isPresent());
        Map<Long, String> buttons = message.getButtonsContext().get().getVariants();
        assertEquals(2, buttons.size());
        assertEquals("Экспонат 1", buttons.get(1L));
        assertEquals("Экспонат 2", buttons.get(2L));
        Mockito.verify(userService, Mockito.times(1)).getUserEventsAfterNow(1L);
        Mockito.verify(exhibitService, Mockito.times(1)).getMuseumExhibits(1L);
    }

    /**
     * Если пользователь не на выставке, то команда для него недоступна
     */
    @Test
    void getMessageIfNoEvent(){
        Mockito.when(userService.getUserEventsAfterNow(1L)).thenReturn(List.of());
        Message message = preViewExhibitCommand.getMessage(commandArgs);
        Assertions.assertEquals("Выставка ещё не началась. Эта команда недоступна", message.getText());
        Assertions.assertFalse(message.getButtonsContext().isPresent());
    }

    /**
     * Если в базе нет информации об экспонатах на мероприятии
     */
    @Test
    void getMessageIfNoExhibits(){
        Mockito.when(userService.getUserEventsAfterNow(1L)).thenReturn(List.of(event1));
        Mockito.when(exhibitService.getMuseumExhibits(1L)).thenReturn(List.of());
        Message message = preViewExhibitCommand.getMessage(commandArgs);
        Assertions.assertEquals("Извините, в базе данных отсутствует информация об экспонатах данного музея",
                message.getText());
        Assertions.assertFalse(message.getButtonsContext().isPresent());
    }

}