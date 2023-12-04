package ru.urfu.museumbot.commands;

import ru.urfu.museumbot.jpa.models.User;

/**
 * Команды, которые начинаются не со /
 */
public interface ExecutableWithState extends Executable {

    /**
     * @return состояние
     */
    State getCommandState();
}
