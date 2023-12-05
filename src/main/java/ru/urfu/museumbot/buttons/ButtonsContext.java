package ru.urfu.museumbot.buttons;

import ru.urfu.museumbot.jpa.models.Event;

import java.util.List;

/**
 * Класс, который хранит информацию необходимую для создания разметки с кнопками.
 */
public class ButtonsContext {
    /**
     * Данные, которые отправляются при нажатии на кнопку,
     * содержат информацию об операции, которая происходит при нажатии.
     */
    protected String callbackData;

    /**
     * Варианты выбора
     */
    protected List<Event> variants;

    public ButtonsContext(String callbackData, List<Event> variants) {
        this.callbackData = callbackData;
        this.variants = variants;
    }

    public String getCallbackData() {
        return callbackData;
    }

    public List<Event> getVariants() {
        return variants;
    }
}
