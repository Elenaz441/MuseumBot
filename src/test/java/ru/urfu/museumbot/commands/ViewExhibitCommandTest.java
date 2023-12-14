package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.museumbot.jpa.models.Exhibit;
import ru.urfu.museumbot.jpa.service.ExhibitService;
import ru.urfu.museumbot.message.Message;

@ExtendWith(MockitoExtension.class)
class ViewExhibitCommandTest {
    private final CommandArgs args;
    @InjectMocks
    private ViewExhibitCommand viewExhibitCommand;
    @Mock
    private ExhibitService exhibitService;
    private Exhibit exhibit;

    public ViewExhibitCommandTest() {
        args = new CommandArgs();
        args.setChatId(1L);
        args.setCallbackData("ViewExhibit 1");


    }

    @BeforeEach
    void setUp() {
        exhibit = new Exhibit();
        exhibit.setId(1L);
        exhibit.setTitle("Test exhibit");
        exhibit.setDescription("Test description");
    }

    @Test
    void getMessage() {
        Mockito.doReturn(exhibit).when(exhibitService).getExhibitById(1L);
        Mockito.doReturn("Название: Test exhibit\n\nОписание: Test description")
                .when(exhibitService).getFormattedString(exhibit);
        Message res = viewExhibitCommand.getMessage(args);
        Assertions.assertEquals("Название: Test exhibit\n\nОписание: Test description", res.getText());
    }
}