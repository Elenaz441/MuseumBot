package ru.urfu.museumbot.dataFormat;

import ru.urfu.museumbot.jpa.models.Museum;

/**
 * Класс для форматирования данных о музее в строку
 */
public class MuseumFormat {

    /**
     * Метод для преобразования Museum в строку
     */
    public String toFormattedString(Museum museum) {
        return String.format("Название: %s\nАдрес: %s\nОписание: %s",
                museum.getTitle(),
                museum.getAddress(),
                museum.getDescription());
    }
}
