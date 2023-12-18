package ru.urfu.museumbot.jpa.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.museumbot.jpa.models.Exhibit;

/**
 * Класс для тестирования класса {@link ExhibitService}
 */
@ExtendWith(MockitoExtension.class)
public class ExhibitServiceTest {

    @InjectMocks
    private ExhibitService exhibitService;

    /**
     * Проверка преобразования Event в строку
     */
    @Test
    public void getFormattedString() {
        Exhibit exhibit = new Exhibit();
        exhibit.setId(1L);
        exhibit.setDescription("Test description");
        exhibit.setTitle("Test exhibit");
        Assertions.assertEquals("Название: Test exhibit\n\nОписание: Test description",
                exhibitService.getFormattedString(exhibit));
    }
}