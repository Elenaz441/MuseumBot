package ru.urfu.museumbot.buttons;

import ru.urfu.museumbot.jpa.models.Event;

import java.util.List;
import java.util.Map;

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
    protected Map<Long, String> variants;

    public ButtonsContext(String callbackData, Map<Long, String> variants) {
        this.callbackData = callbackData;
        this.variants = variants;
    }

    public String getCallbackData() {
        return callbackData;
    }

    public Map<Long, String> getVariants() {
        return variants;
    }
}
