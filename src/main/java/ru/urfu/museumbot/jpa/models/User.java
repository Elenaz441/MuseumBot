package ru.urfu.museumbot.jpa.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.*;


/**
 * <p>Модель таблицы "Пользователь"</p>
 * <p>{@link User#id} уникальный идентификатор пользователя</p>
 * <p>{@link User#chatId} идентификатор чата пользователя</p>
 * <p>{@link User#name} имя</p>
 * <p>{@link User#settingReminders} флаг на разрешение пользователя присылать напоминание о предстоящем мероприятии</p>
 * <p>{@link User#randomExposureSetting} флаг на разрешение рассылки о случайном экспонате</p>
 * <p>{@link User#notificationTime} время получения уведомлений и напоминаний</p>
 * <p>{@link User#reviews} отзывы пользователя</p>
 * <p>{@link User#notifications} уведомления пользователя</p>
 *
 */
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long chatId;

    private String name;

    private boolean settingReminders = true;

    private boolean randomExposureSetting = false;

    private Date notificationTime = new GregorianCalendar(2000, Calendar.JANUARY, 1, 14, 0).getTime();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();

    /**
     * Состояние
     */
    private String state = "Init";

    /**
     * Идентификатор отзыва, который пользователь оставляет в данный момент времени
     * Поле задаётся во время выполнения команды /leave_review
     */
    public Long reviewingEvent = null;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSettingReminders() {
        return settingReminders;
    }

    public void setSettingReminders(boolean settingReminders) {
        this.settingReminders = settingReminders;
    }

    public boolean isRandomExposureSetting() {
        return randomExposureSetting;
    }

    public void setRandomExposureSetting(boolean randomExposureSetting) {
        this.randomExposureSetting = randomExposureSetting;
    }

    public Date getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(Date notificationTime) {
        this.notificationTime = notificationTime;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getReviewingEvent() {
        return reviewingEvent;
    }

    public void setReviewingEvent(Long reviewingEvent) {
        this.reviewingEvent = reviewingEvent;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    /**
     * добавляет отзыв в список
     * @param review отзыв, который нужно добавить
     */
    public void addReview(Review review) {
        reviews.add(review);
        review.setUser(this);
    }

    /**
     * <p>Удалить отзыв из списка</p>
     * @param review - отзыв, который нужно удалить
     */
    public void removeReview(Review review) {
        reviews.remove(review);
        review.setUser(null);
    }

    public void removeNotification(Notification notification) {
        notifications.remove(notification);
        notification.setUser(null);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", settingReminders=" + settingReminders +
                ", randomExposureSetting=" + randomExposureSetting +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
