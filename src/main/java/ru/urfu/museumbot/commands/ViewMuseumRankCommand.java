package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.dataFormat.ReviewFormat;
import ru.urfu.museumbot.jpa.models.Review;
import ru.urfu.museumbot.jpa.service.MuseumService;
import ru.urfu.museumbot.message.Message;

import java.util.List;
import java.util.stream.Collectors;

import static ru.urfu.museumbot.commands.Commands.GET_RANK;

/**
 * Команда для просмотра рейтинга и отзывов музея
 */
@Service
public class ViewMuseumRankCommand implements Command {

    private final MuseumService museumService;

    @Autowired
    public ViewMuseumRankCommand(MuseumService museumService) {
        this.museumService = museumService;
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public Message getMessage(CommandArgs args) {
        return new Message(
                args.getChatId(),
                viewMuseumRank(args.getCallbackData()));
    }

    @Override
    public String getCommandName() {
        return GET_RANK;
    }

    /**
     * <p>Посмотреть последние 10 отзывов о музее</p>
     */
    private String viewMuseumRank(String callbackData) {
        ReviewFormat reviewFormat = new ReviewFormat();
        String text = "Средняя оценка от пользователей: ";
        Long museumId = Long.valueOf(callbackData.replace(GET_RANK + " ", ""));
        List<Review> reviews = museumService.getMuseumReviews(museumId);
        String rank = museumService.getMuseumRank(museumId);
        if (reviews.size() > 10) {
            reviews = reviews.subList(reviews.size() - 11, reviews.size() - 1);
        }
        String textReviews = reviews
                .stream()
                .map(reviewFormat::toFormattedString)
                .collect(Collectors.joining("\n\n===============================\n\n"));
        return text + rank + ". Ниже несколько последних отзывов: \n" + textReviews;
    }
}
