package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.Review;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.service.EventService;
import ru.urfu.museumbot.jpa.service.ReviewService;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

import static ru.urfu.museumbot.commands.Commands.ADD_EVENT;

/**
 * Класс для команды регистрации пользователя на мероприятие
 */
@Service
public class SignUpCommand implements Command {

    private static final String MESSAGE_SUCCESS = "Вы записались на выбранное мероприятие";
    private final EventService eventService;
    private final ReviewService reviewService;
    private final UserService userService;

    @Autowired
    public SignUpCommand(EventService eventService,
                         ReviewService reviewService,
                         UserService userService) {
        this.eventService = eventService;
        this.reviewService = reviewService;
        this.userService = userService;
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public Message getMessage(CommandArgs args) {
        Long chatId = args.getChatId();
        String callback = args.getCallbackData();
        return new Message(chatId, signUp(callback, chatId));
    }

    @Override
    public String getCommandName() {
        return ADD_EVENT;
    }

    /**
     * Регистрация пользователя на выбранное мероприятие
     */
    private String signUp(String callbackData, Long chatId) {
        String text = MESSAGE_SUCCESS;
        Long eventId = Long.valueOf(callbackData.replace(ADD_EVENT + " ", ""));
        Review review = new Review();
        User user = userService.getUserByChatId(chatId);
        Event event = eventService.getEventById(eventId);

        Review prevReview = reviewService.getReview(user, event);
        if (prevReview == null) {
            review.setUser(user);
            review.setEvent(event);
            reviewService.addReview(review);
        } else {
            text = String.format("Вы уже записаны на мероприятие \"%s\"", prevReview.getEvent().getTitle());
        }
        return text;
    }
}
