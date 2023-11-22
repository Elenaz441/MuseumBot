package ru.urfu.museumbot.jpa.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * <p>Модель таблицы "Мероприятия"</p>
 * <p>{@link Event#id} уникальный идентификатор мероприятия </p>
 * <p>{@link Event#title} Название </p>
 * <p>{@link Event#description} Описание </p>
 * <p>{@link Event#date} дата/время проведения </p>
 * <p>{@link Event#duration} длительность мероприятия </p>
 * <p>{@link Event#address} Место проведения/адрес </p>
 * <p>{@link Event#reviews} Отзывы о мероприятии </p>
 */
@Entity
@Table(name = "museum_event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String title;

    @Lob
    @Column(columnDefinition = "text")
    private String description;

    private Date date;

    private int duration;

    private String address;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviews;

    public Event() {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    /**
     * <p>Удалить отзыв из списка</p>
     * @param review - отзыв, который нужно удалить
     */
    public void removeReview(Review review) {
        reviews.remove(review);
        review.setEvent(null);
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy, HH:mm");
        return String.format("%s\n\n%s\n\nДата: %s\nДлительность: %s минут\nАдрес: %s", title, description,
                dateFormat.format(date), duration, address);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id.equals(event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * добавить отзыв в список
     * @param review отзыв, который нужно добавить
     */
    public void addReview(Review review) {
        reviews.add(review);
        review.setEvent(this);
    }
}
