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

@Service
public class CommentEventNonCommand implements ExecutableWithState {

    private final EventService eventService;
    private final ReviewService reviewService;
    public static final String COMMENT_MESSAGE = "Спасибо, что оставили отзыв на мероприятие";
    private final UserService userService;
    private Event commentedEvent;

@Autowired
    public CommentEventNonCommand(UserService userService, EventService eventService, ReviewService reviewService) {
        this.eventService = eventService;
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @Override
    public Message getMessage(CommandArgs args) {
        String userComment = args.getUserInput();
        Long chatId = args.getChatId();
        userService.updateUserState(chatId, State.INIT);
        leaveComment(chatId, userComment);
        return new Message(chatId, String.format("%s '%s'",COMMENT_MESSAGE, commentedEvent.getTitle()));
    }
    /**
     * Сохраняет комментарий оставленный пользователем
     * @param chatId чат пользователя
     * @param userComment комментарий
     */
    private void leaveComment(Long chatId, String userComment) {
        User user = userService.getUserByChatId(chatId);
        Long eventId = user.getReviewingEvent();
        this.commentedEvent = eventService.getEventById(eventId);
        Review review = reviewService.getReview(user, commentedEvent);
        review.setReview(userComment);
        reviewService.updateReview(review);
    }

    @Override
    public State getCommandState() {
        return State.COMMENT;
    }
}
