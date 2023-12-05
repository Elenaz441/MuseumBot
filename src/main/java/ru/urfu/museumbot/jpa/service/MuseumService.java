package ru.urfu.museumbot.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.jpa.models.Exhibit;
import ru.urfu.museumbot.jpa.models.Museum;
import ru.urfu.museumbot.jpa.repository.MuseumRepository;
/**
 * <p>Класс для работы с данными из бд для сущности "Организация"</p>
 */
@Service
public class MuseumService {
    private final MuseumRepository museumRepository;
    @Autowired
    public MuseumService(MuseumRepository museumRepository) {
        this.museumRepository = museumRepository;
    }

    /**
     * <p>Получить организацию по id</p>
     */
    public Museum getExhibitById(Long id) {
        return museumRepository.getMuseumById(id);
    }
}
