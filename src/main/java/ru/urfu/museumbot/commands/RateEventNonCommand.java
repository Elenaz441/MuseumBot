package ru.urfu.museumbot.commands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.customException.UserInputException;
import ru.urfu.museumbot.jpa.service.ReviewService;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

@Service
public class RateEventNonCommand implements ExecutableWithState {
    private final ReviewService reviewService;
    public static final String RATE_SUCCESS_MESSAGE = "Напишите, пожалуйста, небольшой отзыв." +
            " Что вам понравилось/запомнилось больше всего.";
    public static final String RATE_FAILURE_MESSAGE = "Пожалуйста, введите число";
    private final UserService userService;

    @Autowired
    public RateEventNonCommand (UserService userService, ReviewService reviewService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @Override
    public Message getMessage(CommandArgs args) {
        Long chatId = args.getChatId();
        String userInput = args.getUserInput();
        if (!validateUserInput(userInput)) {
            userService.updateUserState(chatId, State.RATE);
            return new Message(chatId, RATE_FAILURE_MESSAGE);
        }
        double rating = Double.parseDouble(userInput);
        reviewService.rateEvent(chatId, rating);
        userService.updateUserState(chatId, State.COMMENT);
        return new Message(chatId, RATE_SUCCESS_MESSAGE);
    }

    private boolean validateUserInput(String userInput) {
        try{
            double rating = Double.parseDouble(userInput);
            if (!(rating >= 0 && rating <= 10)) {
                throw new UserInputException("Оценивание происходит по десятибалльной шкале", userInput);
            }
            return true;
        }catch (NumberFormatException | UserInputException e){
            System.out.printf("Оценка не поставилась! Причина: %s\n", e.getMessage());
        }
        return false;
    }

    @Override
    public State getCommandState() {
        return State.RATE;
    }

}
