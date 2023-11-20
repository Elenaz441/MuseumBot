package ru.urfu.museumbot.JPA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.museumbot.JPA.models.Exhibit;
/**
 * <p>Репозиторий сущности "Экспонат"</p>
 */
@Repository
public interface ExhibitRepository extends JpaRepository<Exhibit, Long> {
    Exhibit getExhibitById(Long id);
}