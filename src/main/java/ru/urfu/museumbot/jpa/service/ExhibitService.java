package ru.urfu.museumbot.JPA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.JPA.models.Event;
import ru.urfu.museumbot.JPA.models.Exhibit;
import ru.urfu.museumbot.JPA.repository.ExhibitRepository;

@Service
public class ExhibitService {
    private final ExhibitRepository exhibitRepository;
    @Autowired
    public ExhibitService(ExhibitRepository exhibitRepository) {
        this.exhibitRepository = exhibitRepository;
    }
    public Exhibit getExhibitById(Long id) {
        return exhibitRepository.getExhibitById(id);
    }

}
