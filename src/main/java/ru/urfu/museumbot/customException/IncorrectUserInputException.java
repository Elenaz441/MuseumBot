package ru.urfu.museumbot.customException;

/**
 * Класс исключения на некорректный ввод пользователя
 */
public class IncorrectUserInputException extends Exception {
    public IncorrectUserInputException(String message) {
        super(message);
    }
}