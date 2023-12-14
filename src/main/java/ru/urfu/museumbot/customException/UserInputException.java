package ru.urfu.museumbot.customException;

/**
 * Костомный класс исключения на некорректный ввод пользователя
 */
public class UserInputException extends Exception {
    public UserInputException(String message) {
        super(message);
    }
}