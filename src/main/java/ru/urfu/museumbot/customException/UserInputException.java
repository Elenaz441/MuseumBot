package ru.urfu.museumbot.customException;

public class UserInputException extends Exception {
    public UserInputException(String message) {
        super(message);
    }
}