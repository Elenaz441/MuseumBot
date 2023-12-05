package ru.urfu.museumbot.JPA.models;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * <p>Модель таблицы "Экспонаты"</p>
 * <p>{@link Exhibit#id} уникальный идентификатор экспоната </p>
 * <p>{@link Exhibit#title} Название </p>
 * <p>{@link Exhibit#description} Описание </p>
 *  <p>{@link Exhibit#museum} Организация, которой принадлежит экспонат</p>
 *  */
@Entity
@Table(name="exhibit")
public class Exhibit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name="title")
    private String title;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "museum_exhibit_id"), name = "museum_exhibit_id")
    private Museum museum;

    public Exhibit() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Museum getMuseum() {
        return museum;
    }

    public void setMuseum(Museum museum) {
        this.museum = museum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exhibit exhibit = (Exhibit) o;
        return Objects.equals(id, exhibit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Exhibit{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", museum=" + museum +
                '}';
    }
}
