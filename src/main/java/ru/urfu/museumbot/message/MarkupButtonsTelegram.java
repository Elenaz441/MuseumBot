package ru.urfu.museumbot.message;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.museumbot.buttons.ConvertableMarkupInline;
import ru.urfu.museumbot.jpa.models.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * класс, который хранит необходимое для создания разметки с кнопками
 */
public class MarkupButtonsTelegram extends MarkupButtons implements ConvertableMarkupInline {
    public String getCallbackData() {
        return callbackData;
    }
    public List<Event> getVariants() {
        return variants;
    }
    public MarkupButtonsTelegram(String callbackData, List<Event> variants) {
        super(callbackData, variants);
    }

    @Override
    public InlineKeyboardMarkup getMarkupInline() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        for (Event event : variants) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(event.getTitle());
            inlineKeyboardButton.setCallbackData(callbackData+ " " + event.getId());
            rowInline.add(inlineKeyboardButton);
            rowsInline.add(rowInline);
        }
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}

