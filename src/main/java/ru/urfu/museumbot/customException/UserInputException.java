package ru.urfu.museumbot.customException;

public class UserInputException extends Exception {

    private String userInput;

    public String getUserInput() {
        return userInput;
    }

    public UserInputException(String message, String userInput) {
        super(message);
        this.userInput = userInput;
    }
}