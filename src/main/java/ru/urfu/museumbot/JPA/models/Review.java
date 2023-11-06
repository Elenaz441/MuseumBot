package ru.urfu.museumbot.JPA.models;

import jakarta.persistence.*;

import java.util.Objects;


/**
 * <p>Модель таблицы "Отзыв"</p>
 * <p>{@link Review#id} уникальный индектификатор отзыва</p>
 * <p>{@link Review#user} пользователь, который оставил отзыв</p>
 * <p>{@link Review#event} мероприятие, о котором оставили отзыв</p>
 * <p>о выставке {@link Review#event}</p>
 * <p>{@link Review#rating} оценка</p>
 * <p>{@link Review#review} текстовый отзыв/комментарии/обратная связь</p>
 * 
 */
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "user_id"), name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "event_id"), name = "event_id")
    private Event event;

    private  int rating = 0;

    @Lob
    @Column(columnDefinition = "text")
    private String review = null;

    public Review() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event eventId) {
        this.event = eventId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", user=" + user +
                ", event=" + event +
                ", rating=" + rating +
                ", review='" + review + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return id.equals(review.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
