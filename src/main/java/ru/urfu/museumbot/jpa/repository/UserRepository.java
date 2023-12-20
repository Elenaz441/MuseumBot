package ru.urfu.museumbot.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.museumbot.jpa.models.User;

import java.util.List;


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

    /**
     * Получить список пользователей по настройке получать информацию о рандомном экспонате
     */
    List<User> getUserByRandomExposureSetting(Boolean randomExposureSetting);
}
