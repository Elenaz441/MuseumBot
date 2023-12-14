package ru.urfu.museumbot.commands;

import ru.urfu.museumbot.message.Message;

/**
 * Базовый интерфейс любой команды, со стрейтом и без
 * Абстрактный объект, который может вести себя как команда, т.е. выполняться
 */
public interface Executable {
    /**
     * Основной метод, который вызывает работу команды
     */
    Message getMessage(CommandArgs args);
}
