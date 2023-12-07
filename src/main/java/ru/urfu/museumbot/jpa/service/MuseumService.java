package ru.urfu.museumbot.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.Review;
import ru.urfu.museumbot.jpa.models.Museum;
import ru.urfu.museumbot.jpa.repository.MuseumRepository;
import java.util.List;
import java.util.Comparator;

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
    public Museum getMuseumById(Long id) {
        return museumRepository.getMuseumById(id);
    }

    /**
     * Получить список всех музеев
     */
    public List<Museum> getMuseums() {
        return museumRepository.findAll();
    }

    /**
     * Получить все отзывы музея
     */
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

    /**
     * Посчитать среднее значение оценок
     */
    public String getMuseumRank(Long id) {
        List<Review> reviews = getMuseumReviews(id);
        Double rank = reviews.stream()
                .map(Review::getRating)
                .mapToInt(Double::intValue)
                .average()
                .orElse(-1);
        String result = String.valueOf(rank);
        if (result.equals("-1.0")) {
            result = "Оценок ещё нет";
        }
        return result;
    }
}
