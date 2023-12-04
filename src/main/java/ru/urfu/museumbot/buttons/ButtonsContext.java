package ru.urfu.museumbot.buttons;

import ru.urfu.museumbot.jpa.models.Event;

import java.util.List;

/**
 * абстрактный класс, который хранит информацию необходимую для создания разметки с кнопками
 */
public class ButtonsContext {
    /**
     * Данные, которые отправляются при нажатии на кнопку,
     * содержат информацию об опреации которая происходит при нажатии
     */
    protected String callbackData;

    public String getCallbackData() {
        return callbackData;
    }

    public List<Event> getVariants() {
        return variants;
    }

    /**
     * Варианты выбора
     */
    protected List<Event> variants;
    public ButtonsContext(String callbackData, List<Event> variants) {
        this.callbackData = callbackData;
        this.variants = variants;
    }
}
