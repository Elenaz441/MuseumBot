package ru.urfu.museumbot.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.urfu.museumbot.jpa.models.Event;

import java.util.List;

/**
 * классы, реализующие этот интерфейс
 * могут преобразовываться в разметку с кнопками
 */
public interface ConvertableMarkupInline {
    /**
     * @return разметку с кнопками
     */
    InlineKeyboardMarkup getMarkupInline();
}
