package ru.urfu.museumbot.dataFormat;
import ru.urfu.museumbot.jpa.models.Exhibit;

/**
 * Класс форматирования для вывода пользователю {@link ru.urfu.museumbot.jpa.models.Exhibit}
 */
public class ExhibitFormat {

    /**
     * Метод для преобразования Exhibit в строку
     */
    public String toFormattedString(Exhibit exhibit) {
        return String.format("Название: %s\n\nОписание: %s",
                exhibit.getTitle(),
                exhibit.getDescription());
    }
}
