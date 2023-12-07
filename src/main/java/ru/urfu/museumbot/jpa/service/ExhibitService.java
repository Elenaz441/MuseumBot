package ru.urfu.museumbot.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.jpa.models.Exhibit;
import ru.urfu.museumbot.jpa.repository.ExhibitRepository;

import java.util.List;

/**
 * <p>Класс для работы с данными из бд для сущности "Экспонат"</p>
 */
@Service
public class ExhibitService {
    private final ExhibitRepository exhibitRepository;
    @Autowired
    public ExhibitService(ExhibitRepository exhibitRepository) {
        this.exhibitRepository = exhibitRepository;
    }
    /**
     * <p>Получить экспонат по id</p>
     */
    public Exhibit getExhibitById(Long id) {
        return exhibitRepository.getExhibitById(id);
    }
    /**
     * Получить все экспонаты из музея в котором проиходит мероприятие
     * @param eventId Идентификатор мероприятия
     */
    public List<Exhibit> getMuseumExhibits(Long eventId){
        return exhibitRepository.getMuseumExhibits(eventId);
    }
}
