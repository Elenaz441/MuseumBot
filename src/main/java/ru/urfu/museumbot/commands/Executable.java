package ru.urfu.museumbot.commands;

import ru.urfu.museumbot.message.Message;

public interface Executable {
    /**
     * Основной метод, который вызывает работу команды
     */
    Message getMessage(CommandArgs args);
}
