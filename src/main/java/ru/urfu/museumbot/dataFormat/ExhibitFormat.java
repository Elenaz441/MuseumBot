package ru.urfu.museumbot.dataFormat;
import ru.urfu.museumbot.jpa.models.Exhibit;

/**
 * Класс форматирования для вывода пользователю {@link ru.urfu.museumbot.jpa.models.Exhibit}
 */
public class ExhibitFormat {

    /**
     * Метод для преобразования {@link Exhibit} в строку
     */
    public String toFormattedString(Exhibit exhibit) {
        return String.format("Название: %s\n\nОписание: %s",
                exhibit.getTitle(),
                exhibit.getDescription());
    }

    /**
     * Преобразует {@link Exhibit} в строку нужного формата для отправки рассылки об экспонате
     */
    public String toFormattedStringForDistribution(Exhibit exhibit){
        return String.format("%s\n\n%s\n\nДанный экспонат вы можете увидеть в %s",
                exhibit.getTitle(),
                exhibit.getDescription(),
                exhibit.getMuseum().getTitle());
    }
}
