package ru.urfu.museumbot.JPA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.JPA.models.Exhibit;
import ru.urfu.museumbot.JPA.models.Museum;
import ru.urfu.museumbot.JPA.repository.MuseumRepository;
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

    public Museum getExhibitById(Long id) {
        return museumRepository.getMuseumById(id);
    }
}
