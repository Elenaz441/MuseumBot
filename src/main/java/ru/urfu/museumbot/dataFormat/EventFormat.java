package ru.urfu.museumbot.dataFormat;

import ru.urfu.museumbot.jpa.models.Event;

import java.text.SimpleDateFormat;

/**
 * Класс для форматирования данных о мероприятии в строку
 */
public class EventFormat {

    /**
     * Метод для преобразования Event в строку
     */
    public String toFormattedString(Event event) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy, HH:mm");
        return String.format("%s\n\n%s\n\nДата: %s\nДлительность: %s минут\nАдрес: %s",
                event.getTitle(),
                event.getDescription(),
                dateFormat.format(event.getDate()),
                event.getDuration(),
                event.getAddress());
    }

    /**
     * Метод форматирования мероприятия в строку для напоминания
     */
    public String toFormattedStringForNotification(Event event) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return String.format("Мероприятие \"%s\" состоится завтра в %s по адресу %s (%s).",
                event.getTitle(),
                dateFormat.format(event.getDate()),
                event.getAddress(),
                event.getMuseum().getTitle());
    }

}
