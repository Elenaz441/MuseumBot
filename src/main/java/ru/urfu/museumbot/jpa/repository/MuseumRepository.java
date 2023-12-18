package ru.urfu.museumbot.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.museumbot.jpa.models.Museum;

/**
 * <p>Репозиторий сущности "Организация"</p>
 */
public interface MuseumRepository  extends JpaRepository<Museum, Long> {
    /**
     * @param id идентификатор музея
     * @return музей c идентификатором id
     */
    Museum getMuseumById(Long id);
}
