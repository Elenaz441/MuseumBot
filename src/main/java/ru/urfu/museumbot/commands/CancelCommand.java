package ru.urfu.museumbot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.urfu.museumbot.jpa.models.Review;
import ru.urfu.museumbot.jpa.service.*;

import static ru.urfu.museumbot.commands.Commands.CANCEL_EVENT;

/**
 * Класс для команды отмены регистрации на мероприятие
 */
public class CancelCommand implements Command {

    private final EventService eventService;
    private final ReviewService reviewService;
    private final UserService userService;

    public CancelCommand(EventService eventService,
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
        message.setText(cancel(callback, chatId));
        return message;
    }

    /**
     * Отмена регистрации на выбранное мероприятие
     */
    private String cancel(String callbackData, Long chatId) {
        String text = "Вы отменили свою запись на выбранное мероприятие";
        Long eventId = Long.valueOf(callbackData.replace(CANCEL_EVENT + " ", ""));
        Review review = reviewService.getReview(
                userService.getUserByChatId(chatId),
                eventService.getEventById(eventId));
        if (review == null) {
            text = "Вы не записаны на данное мероприятие";
        } else {
            reviewService.deleteReview(review);
        }
        return text;
    }

}
