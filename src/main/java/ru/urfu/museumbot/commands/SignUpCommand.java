package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.Review;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.service.*;

import static ru.urfu.museumbot.commands.Commands.ADD_EVENT;

/**
 * Класс для команды регистрации пользователя на мероприятие
 */
@Service
public class SignUpCommand implements Command {

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
    public SendMessage getMessage(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String callback = update.getCallbackQuery().getData();
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(signUp(callback, chatId));
        return message;
    }

    @Override
    public String getCommandName() {
        return ADD_EVENT;
    }

    /**
     * Регистрация пользователя на выбранное мероприятие
     */
    private String signUp(String callbackData, Long chatId) {
        String text = "Вы записались на выбранное мероприятие";
        Long eventId = Long.valueOf(callbackData.replace(ADD_EVENT + " ", ""));
        Review review = new Review();
        User user = userService.getUserByChatId(chatId);
        Event event = eventService.getEventById(eventId);

        Review prevReview = reviewService.getReview(user, event);
        if (prevReview == null) {
            review.setUser(user);
            review.setEvent(event);
            reviewService.addReview(review);
        }
        else {
            text = String.format("Вы уже записаны на мероприятие \"%s\"", prevReview.getEvent().getTitle());
        }
        return text;
    }
}
