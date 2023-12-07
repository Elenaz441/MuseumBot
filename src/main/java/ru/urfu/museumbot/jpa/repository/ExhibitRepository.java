package ru.urfu.museumbot.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.urfu.museumbot.jpa.models.Exhibit;

import java.util.List;

/**
 * <p>Репозиторий сущности "Экспонат"</p>
 */
@Repository
public interface ExhibitRepository extends JpaRepository<Exhibit, Long> {
    Exhibit getExhibitById(Long id);
    @Query(value = "SELECT * from exhibit where exhibit.museum_exhibit_id = " +
            "(SELECT id from museum where museum.id = " +
            "(select museum_event_id from museum_event where museum_event.id = :eventid))", nativeQuery = true)
    List<Exhibit> getMuseumExhibits(Long eventid);
}
