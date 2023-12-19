package ru.urfu.museumbot.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.Notification;
import ru.urfu.museumbot.jpa.models.User;

import java.util.Date;
import java.util.List;

/**
 * <p>Репозиторий сущности "Уведомление"</p>
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Получить все уведомления после данной даты
     */
    List<Notification> getNotificationsBySendingDateAfter(Date date);

    /**
     * Получить все уведомления перед данной даты
     */
    List<Notification> getNotificationsBySendingDateBefore(Date date);

    /**
     * Получить уведомление по пользователю и мероприятию
     */
    Notification getNotificationByUserAndEvent(User user, Event event);
}
