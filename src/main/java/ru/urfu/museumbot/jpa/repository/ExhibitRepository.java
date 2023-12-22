package ru.urfu.museumbot.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.museumbot.jpa.models.Exhibit;


/**
 * <p>Репозиторий сущности "Экспонат"</p>
 */
@Repository
public interface ExhibitRepository extends JpaRepository<Exhibit, Long> {
    /**
     * @param id идентификатор экспоната
     * @return экспонат c идентификатором id
     */
    Exhibit getExhibitById(Long id);

    /**
     * @return количество записей в таблице Экспонаты
     */
    long count();
}
