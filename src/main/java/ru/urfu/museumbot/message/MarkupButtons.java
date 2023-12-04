package ru.urfu.museumbot.message;

import ru.urfu.museumbot.jpa.models.Event;

import java.util.List;

abstract class MarkupButtons {
    protected String callbackData;
    protected List<Event> variants;
    public MarkupButtons(String callbackData, List<Event> variants) {
        this.callbackData = callbackData;
        this.variants = variants;
    }
}
