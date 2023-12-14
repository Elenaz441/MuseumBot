package ru.urfu.museumbot.commands;

/**
 * Команды, которые начинаются не со /
 */
public interface ExecutableWithState extends Executable {

    /**
     * Состояние - главное, что отличает {@link ru.urfu.museumbot.commands.Command}
     * от {@link ru.urfu.museumbot.commands.ExecutableWithState}
     * этот метод возвращает индентификатор для определения команды
     * @return состояние
     */
    State getCommandState();
}
