package ru.urfu.museumbot.jpa.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

/**
 * Модель таблицы "Уведомление"
 * <p>{@link Notification#id} уникальный идентификатор уведомления</p>
 * <p>{@link Notification#user} пользователь, которому отправится уведомление</p>
 * <p>{@link Notification#event} мероприятие, о котором нужно напомнить</p>
 * <p>{@link Notification#text} текст уведомления</p>
 * <p>{@link Notification#sendingDate} дата отправки</p>
 */
@Entity
@Table(name = "notification_user")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "user_not_id"), name = "user_not_id")
    private User user;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "event_not_id"), name = "event_not_id")
    private Event event;

    @Lob
    @Column(columnDefinition = "text")
    private String text;

    private Date sendingDate;

    public Notification() {
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

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getSendingDate() {
        return sendingDate;
    }

    public void setSendingDate(Date sendingDate) {
        this.sendingDate = sendingDate;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", user=" + user +
                ", text='" + text + '\'' +
                ", sendingDate=" + sendingDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
