package ru.urfu.museumbot.commands;

/**
 * Команды, которые начинаются не со /
 */
public interface ExecutableWithState extends Executable {

    /**
     * @return состояние
     */
    State getCommandState();
}
