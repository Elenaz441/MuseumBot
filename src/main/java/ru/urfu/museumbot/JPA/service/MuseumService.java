package ru.urfu.museumbot.JPA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.JPA.models.Event;
import ru.urfu.museumbot.JPA.models.Museum;
import ru.urfu.museumbot.JPA.models.Review;
import ru.urfu.museumbot.JPA.repository.MuseumRepository;

import java.util.Comparator;
import java.util.List;

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

    /**
     * Получить список всех музеев
     */
    public List<Museum> getMuseums() {
        return museumRepository.findAll();
    }

    public List<Review> getMuseumReviews(Long id) {
        Museum museum = museumRepository.getMuseumById(id);
        return museum.getEvents()
                .stream()
                .map(Event::getReviews)
                .flatMap(List::stream)
                .filter(review -> review.getRating() != 0)
                .sorted(Comparator.comparing(review -> review.getEvent().getDate()))
                .toList();
    }

    public String getMuseumRank(Long id) {
        List<Review> reviews = getMuseumReviews(id);
        Double rank = reviews.stream()
                .map(Review::getRating)
                .mapToInt(r -> r)
                .average()
                .orElse(-1);
        String result = String.valueOf(rank);
        if (result.equals("-1")) {
            result = "Оценок ещё нет";
        }
        return result;
    }
}
