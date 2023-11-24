package ru.urfu.museumbot.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.Review;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.service.*;

import static ru.urfu.museumbot.commands.Commands.ADD_EVENT;

/**
 * Класс для команды регистрации пользователя на мероприятие
 */
public class SignUpCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final EventService eventService;
    private final ReviewService reviewService;
    private final UserService userService;

    public SignUpCommand(SendBotMessageService sendBotMessageService, ServiceContext serviceContext) {
        this.sendBotMessageService = sendBotMessageService;
        this.eventService = serviceContext.getEventService();
        this.reviewService = serviceContext.getReviewService();
        this.userService = serviceContext.getUserService();
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public void execute(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String callback = update.getCallbackQuery().getData();
        sendBotMessageService.sendMessage(chatId.toString(), signUp(callback, chatId));
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
