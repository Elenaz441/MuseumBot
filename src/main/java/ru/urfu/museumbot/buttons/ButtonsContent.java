package ru.urfu.museumbot.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.museumbot.jpa.models.Event;

import java.util.ArrayList;
import java.util.List;

public class ButtonsContent {

    /**
     * Создаёт пользовательский итерфейс регитсрации и отмены регистрации на мероприятия {@link ButtonsContext#variants}
     * Нажимая на одну из кнопок пользователь совержает выбранную опцию (регистрации/отмены) этого мероприятия
     * @return разметку в виде кнопок с возможностью выбрать мероприятие
     */
    public InlineKeyboardMarkup getMarkupInline(ButtonsContext buttonsContext) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        for (Event event : buttonsContext.variants) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(event.getTitle());
            inlineKeyboardButton.setCallbackData(buttonsContext.callbackData+ " " + event.getId());
            rowInline.add(inlineKeyboardButton);
            rowsInline.add(rowInline);
        }
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}
