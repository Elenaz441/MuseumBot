package ru.urfu.museumbot.JPA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.museumbot.JPA.models.Museum;

/**
 * <p>Репозиторий сущности "Организация"</p>
 */
public interface MuseumRepository  extends JpaRepository<Museum, Long> {
    Museum getMuseumById(Long id);
}
