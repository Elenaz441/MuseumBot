package ru.urfu.museumbot.dataFormat;

import ru.urfu.museumbot.jpa.models.Review;

/**
 * Класс для форматирования данных об отзыве в строку
 */
public class ReviewFormat {

    /**
     * Метод для преобразования Review в строку
     */
    public String toFormattedString(Review review) {
        return String.format("Мероприятие: %s\nОценка: %s\nОтзыв: %s",
                review.getEvent().getTitle(),
                review.getRating(),
                review.getReview());
    }
}
