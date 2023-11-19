package ru.urfu.museumbot.GUI;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.museumbot.JPA.models.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Map.entry;
import static ru.urfu.museumbot.commands.Commands.*;

/**
 * <p>класс виджетов</p>
 * <p>предоставляет активные элементы для оботражения сообщений от бота</p>
 */
public class Widgets {
    /**
     * Отображение, которое сожержит ключ:команда значение:описание
     */
    public static Map<String, String> description = Map.ofEntries(
            entry(START, "Старт"),
            entry(HELP, "Нужна помощь?"),
            entry(VIEW_UPCOMING_EVENTS, "Посмотреть ближайшие мероприятия"),
            entry(SIGN_UP_FOR_EVENT, "Зарегистрироваться на мероприятие"),
            entry(CANCEL, "Отменить запись на мероприятие"),
            entry(VIEW_MY_EVENTS, "Посмотреть на записанные мероприятия")
    );

    /**
     * @return Список из команд, которые отображаются в меню
     */
    public List<BotCommand> getMenuOfCommands() {
        Set<Map.Entry<String, String>> descriptionCommands = description.entrySet();
        return descriptionCommands
                .stream()
                .map(entry->new BotCommand(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Создаёт графический интерфейс в виде кнопок с выбором
     * @param callbackData в зависимости от того, какая команда сейчас выполняется
     * @param variants варианты выбора для пользователя
     * @return виджет последоватлеьной разметки кнопками
     */
    public InlineKeyboardMarkup getMarkupInline(String callbackData, List<Event> variants){
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        for (Event event : variants) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(event.getTitle());
            inlineKeyboardButton.setCallbackData(callbackData + event.getId());
            rowInline.add(inlineKeyboardButton);
            rowsInline.add(rowInline);
        }
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}
