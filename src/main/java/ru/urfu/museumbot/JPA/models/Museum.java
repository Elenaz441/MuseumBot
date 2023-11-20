package ru.urfu.museumbot.JPA.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>Модель таблицы "Организации"</p>
 * <p>{@link Museum#id} уникальный идентификатор организации</p>
 * <p>{@link Museum#title} Название </p>
 * <p>{@link Museum#description} Описание </p>
 * <p>{@link Museum#address} Место проведения/адрес </p>
 * <p>{@link Museum#exhibits} экспонаты, которые принадлежат организации</p>
 * <p>{@link Museum#events} Мероприятия, которые проходят в организации/p>
 */
@Entity
public class Museum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "title")

    private String title;
    @Column(name = "address")

    private String address;
    @Lob
    @Column(name="description", columnDefinition = "text")
    private String description;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "museum", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Exhibit> exhibits = new ArrayList<>();
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "museum", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Event> events = new ArrayList<>();

    public Museum() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Exhibit> getExhibits() {
        return exhibits;
    }

    public void setExhibits(List<Exhibit> exhibits) {
        this.exhibits = exhibits;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "Название: " + title +
                "\nАдрес: " + address +
                "\nОписание: " + description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Museum museum = (Museum) o;
        return Objects.equals(id, museum.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
