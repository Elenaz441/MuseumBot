package ru.urfu.museumbot.customException;

/**
 * Кастомный класс исключения на некорректный ввод пользователя
 */
public class UserInputException extends Exception {
    public UserInputException(String message) {
        super(message);
    }
}