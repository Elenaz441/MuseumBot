package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.Museum;
import ru.urfu.museumbot.jpa.models.Review;
import ru.urfu.museumbot.jpa.service.MuseumService;
import ru.urfu.museumbot.message.Message;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для тестирования класса {@link ViewMuseumRankCommand}
 */
@ExtendWith(MockitoExtension.class)
class ViewMuseumRankCommandTest {

    @InjectMocks
    ViewMuseumRankCommand viewMuseumRankCommand;

    @Mock
    MuseumService museumService;

    CommandArgs commandArgs;

    List<Review> reviews;

    /**
     * Подготовка данных для тестов
     */
    ViewMuseumRankCommandTest() {
        this.commandArgs = new CommandArgs();
        commandArgs.setChatId(1L);
        commandArgs.setCallbackData("GetRank 1");

        Museum museum = new Museum();
        museum.setId(1L);
        museum.setTitle("Museum 1");
        museum.setDescription("Description 1");
        museum.setAddress("Ленина, 51");

        Event event = new Event();
        event.setId(1L);
        event.setTitle("Event 1");

        Review review1 = new Review();
        review1.setId(1L);
        review1.setRating(5);
        review1.setReview("Review 1");
        review1.setEvent(event);

        Review review2 = new Review();
        review2.setId(2L);
        review2.setRating(7);
        review2.setReview("Review 2");
        review2.setEvent(event);

        reviews = List.of(review1, review2);
    }

    /**
     * Тест на вывод рейтинга и отзывов о музее
     */
    @Test
    void getMessage() {
        Mockito.doReturn(reviews).when(museumService).getMuseumReviews(1L);
        Mockito.doReturn("6.0").when(museumService).getMuseumRank(1L);

        Message message = viewMuseumRankCommand.getMessage(commandArgs);

        assertEquals("""
                Средняя оценка от пользователей: 6.0. Ниже несколько последних отзывов:\s
                Мероприятие: Event 1
                Оценка: 5
                Отзыв: Review 1
                                
                ===============================
                                
                Мероприятие: Event 1
                Оценка: 7
                Отзыв: Review 2""",
                message.getText());

    }
}