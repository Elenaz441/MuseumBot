package ru.urfu.museumbot.JPA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.museumbot.JPA.models.User;


/**
 * <p>Репозиторий сущности "Пользователь"</p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * <p>Проверить, есть ли пользователь с таким chatId</p>
     */
    boolean existsByChatId(Long chatId);

    /**
     * <p>Получить пользователя по chatId</p>
     */
    User getUserByChatId(Long chatId);
}
