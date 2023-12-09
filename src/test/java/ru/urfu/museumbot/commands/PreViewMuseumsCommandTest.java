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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для тестирования класса {@link PreViewMuseumsCommand}
 */
@ExtendWith(MockitoExtension.class)
class PreViewMuseumsCommandTest {

    @InjectMocks
    PreViewMuseumsCommand preViewMuseumsCommand;

    @Mock
    MuseumService museumService;

    CommandArgs commandArgs;

    List<Museum> museums;

    /**
     * Подготовка данных для тестов
     */
    PreViewMuseumsCommandTest() {
        this.commandArgs = new CommandArgs();
        commandArgs.setChatId(1L);

        Museum museum1 = new Museum();
        museum1.setId(1L);
        museum1.setTitle("Museum 1");
        museum1.setDescription("Description 1");
        museum1.setAddress("Ленина, 51");

        Museum museum2 = new Museum();
        museum2.setId(2L);
        museum2.setTitle("Museum 2");
        museum2.setDescription("Description 2");
        museum2.setAddress("Ленина, 52");

        museums = List.of(museum1, museum2);
    }

    /**
     * Тест на вывод музеев с помощью кнопок
     */
    @Test
    void getMessage() {
        Mockito.doReturn(museums).when(museumService).getMuseums();
        Message message = preViewMuseumsCommand.getMessage(commandArgs);

        assertEquals("Выберете музей:", message.getText());
        assertTrue(message.getButtonsContext().isPresent());
        assertEquals(2, message.getButtonsContext().get().getVariants().size());
        assertEquals("GetMuseum", message.getButtonsContext().get().getCallbackData());
        assertEquals("Museum 1", message.getButtonsContext().get().getVariants().get(1L));
        assertEquals("Museum 2", message.getButtonsContext().get().getVariants().get(2L));
    }
}