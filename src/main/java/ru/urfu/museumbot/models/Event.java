package ru.urfu.museumbot.models;

import java.util.Date;
import java.util.Objects;

/**
 * <p>Модель таблицы "Мероприятия"</p>
 * <p>{@link Event#id} уникальный индектификатор мероприятия </p>
 * <p>{@link Event#title} Название </p>
 * <p>{@link Event#description} Описание </p>
 * <p>{@link Event#date} дата/время проведения </p>
 * <p>{@link Event#duration} длительность мероприятия </p>
 * <p>{@link Event#address} Место проведения/адрес </p>
 */
public class Event {
    public Event(Long id, String title, String description, Date date, int duration, String address) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.duration = duration;
        this.address = address;
    }

    public Event() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public int getDuration() {
        return duration;
    }

    public String getAddress() {
        return address;
    }

    private Long id;
    private String title;
    private String description;
    private Date date;
    private int duration;
    private String address;

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", duration=" + duration +
                ", address='" + address + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return duration == event.duration && Objects.equals(id, event.id) && Objects.equals(title, event.title) && Objects.equals(description, event.description) && Objects.equals(date, event.date) && Objects.equals(address, event.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, date, duration, address);
    }
}
