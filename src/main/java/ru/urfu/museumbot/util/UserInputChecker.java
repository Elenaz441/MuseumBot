package ru.urfu.museumbot.util;

import ru.urfu.museumbot.customException.IncorrectUserInputException;

/**
 * Класс, который проверяет булевы настройки из строки пользовательского ввода
 */
public class UserInputChecker {
    /**
     * @param input пользоватлеьский ввод
     * @return булево значение в соответствии с пользовательским вводом.
     * Например, да-true
    */
    public boolean checkUserInputSetting(String input) {
        try{
            if (!input.equalsIgnoreCase("да") && !input.equalsIgnoreCase("нет")) {
                throw new IncorrectUserInputException(String.format("Некорректный ввод: ответ должен содержать" +
                        " только \"да\" или \"нет\""));
            } else {
                return true;
            }
        }
        catch (IncorrectUserInputException e){
            System.out.println("Настройка не выбрана. Причина: " + e.getMessage());
        }
        return false;
    }
}
