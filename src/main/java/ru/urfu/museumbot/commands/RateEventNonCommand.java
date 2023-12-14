package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.customException.UserInputException;
import ru.urfu.museumbot.jpa.service.ReviewService;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

/**
 * Класс отвечающий за этап оценивания, где пользователь вводит оценку
 */
@Service
public class RateEventNonCommand implements ExecutableWithState {

    public static final String RATE_SUCCESS_MESSAGE = "Напишите, пожалуйста, небольшой отзыв." +
            " Что вам понравилось/запомнилось больше всего.";
    public static final String RATE_FAILURE_MESSAGE = "Пожалуйста, введите целое число от 0 до 10";

    private final ReviewService reviewService;
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
        int rating = Integer.parseInt(userInput);
        reviewService.rateEvent(chatId, rating);
        userService.updateUserState(chatId, State.COMMENT);
        return new Message(chatId, RATE_SUCCESS_MESSAGE);
    }

    /**
     * Проверить данные, которые ввёл пользователь, на валидность
     * @param userInput - данные, которые ввёл пользователь
     */
    private boolean validateUserInput(String userInput) {
        try{
            int rating = Integer.parseInt(userInput);

            if (!(rating >= 0 && rating <= 10)) {
                throw new UserInputException(String.format("Оценивание происходит по десятибалльной шкале," +
                        " целыми числами. userInput = %d", rating));
            }
            return true;
        } catch (NumberFormatException | UserInputException e){
            System.out.println("Оценка не поставилась! Причина: " + e.getMessage());
        }
        return false;
    }

    @Override
    public State getCommandState() {
        return State.RATE;
    }

}
