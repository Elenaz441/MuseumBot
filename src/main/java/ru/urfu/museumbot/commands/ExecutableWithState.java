package ru.urfu.museumbot.commands;

import ru.urfu.museumbot.enums.State;

/**
 * Команды, которые начинаются не со /
 */
public interface ExecutableWithState extends Executable {

    /**
     * Состояние - главное, что отличает {@link ru.urfu.museumbot.commands.Command}
     * от {@link ru.urfu.museumbot.commands.ExecutableWithState}
     * этот метод возвращает идентификатор для определения команды
     * @return состояние
     */
    State getCommandState();
}
