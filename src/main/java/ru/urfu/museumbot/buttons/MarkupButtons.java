package ru.urfu.museumbot.buttons;

import ru.urfu.museumbot.jpa.models.Event;

import java.util.List;

/**
 * абстрактный класс, который хранит информацию необходимую для создания разметки с кнопками
 */
abstract class MarkupButtons {
    /**
     * Данные, которые отправляются при нажатии на кнопку,
     * содержат информацию об опреации которая происходит при нажатии
     */
    protected String callbackData;
    /**
     * Варианты выбора
     */
    protected List<Event> variants;
    public MarkupButtons(String callbackData, List<Event> variants) {
        this.callbackData = callbackData;
        this.variants = variants;
    }
}
