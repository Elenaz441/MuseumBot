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

    public String toFormattedStringForNotification(Event event) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return String.format("Завтра в %s состоится мероприятие \"%s\" по адресу %s",
                dateFormat.format(event.getDate()),
                event.getTitle(),
                event.getAddress());
    }

}
