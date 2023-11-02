package ru.urfu.museumbot.models;

import java.util.Objects;

/**
 * <p>Модель таблицы "Отзыв"</p>
 * <p>{@link Review#id} уникальный индектификатор отзыва</p>
 * <p>{@link Review#actionId} id пользователя, который оставил отзыв</p>
 * <p>о выставке с id {@link Review#eventId}</p>
 * <p>{@link Review#rating} оценка</p>
 * <p>{@link Review#review} текстовый отзыв/комментарии/обратная связь</p>
 * 
 */
public class Review {
    private Long id;
    private Long actionId;
    private Long eventId;
    private  int rating;
    private String review;

    public Review(Long id, Long actionId, Long eventId, int rating, String review) {
        this.id = id;
        this.actionId = actionId;
        this.eventId = eventId;
        this.rating = rating;
        this.review = review;
    }

    public Review() {
    }

    public Long getId() {
        return id;
    }

    public Long getActionId() {
        return actionId;
    }

    public Long getEventId() {
        return eventId;
    }

    public int getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review1 = (Review) o;
        return rating == review1.rating && Objects.equals(id, review1.id) && Objects.equals(actionId, review1.actionId) && Objects.equals(eventId, review1.eventId) && Objects.equals(review, review1.review);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, actionId, eventId, rating, review);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", userId=" + actionId +
                ", eventId=" + eventId +
                ", rating=" + rating +
                ", review='" + review + '\'' +
                '}';
    }
}
