package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.museumbot.jpa.models.Museum;
import ru.urfu.museumbot.jpa.service.MuseumService;
import ru.urfu.museumbot.message.Message;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для тестирования класса {@link ViewMuseumCommand}
 */
@ExtendWith(MockitoExtension.class)
class ViewMuseumCommandTest {

    @InjectMocks
    ViewMuseumCommand viewMuseumCommand;

    @Mock
    MuseumService museumService;

    CommandArgs commandArgs;

    Museum museum;

    ViewMuseumCommandTest() {
        this.commandArgs = new CommandArgs();
        commandArgs.setChatId(1L);
        commandArgs.setCallbackData("GetMuseum 1");

        museum = new Museum();
        museum.setId(1L);
        museum.setTitle("Museum 1");
        museum.setDescription("Description 1");
        museum.setAddress("Ленина, 51");
    }

    @Test
    void getMessage() {
        Mockito.doReturn(museum).when(museumService).getMuseumById(1L);

        Message message = viewMuseumCommand.getMessage(commandArgs);

        assertEquals("""
                Название: Museum 1
                Адрес: Ленина, 51
                Описание: Description 1""",
                message.getText());
    }
}