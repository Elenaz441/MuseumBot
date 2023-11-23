package ru.urfu.museumbot.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.Museum;

/**
 * <p>Репозиторий сущности "Организация"</p>
 */
public interface MuseumRepository  extends JpaRepository<Museum, Long> {
    Museum getMuseumById(Long id);
}
