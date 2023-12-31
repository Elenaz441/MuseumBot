package ru.urfu.museumbot.buttons;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.urfu.museumbot.commands.Commands.*;

/**
 * Класс, отвечающий за содержимое кнопок (надписи)
 */
public class ButtonContent {

    /**
     * Отображение, которое содержит ключ:команда значение:описание
     */
    private final List<BotCommand> commands = List.of(
            new BotCommand(START, "Старт"),
            new BotCommand(HELP, "Нужна помощь?"),
            new BotCommand(VIEW_UPCOMING_EVENTS, "Посмотреть ближайшие мероприятия"),
            new BotCommand(SIGN_UP_FOR_EVENT, "Зарегистрироваться на мероприятие"),
            new BotCommand(CANCEL, "Отменить запись на мероприятие"),
            new BotCommand(VIEW_MY_EVENTS, "Посмотреть на записанные мероприятия"),
            new BotCommand(VIEW_EXHIBIT, "Посмотреть информацию об экспонате"),
            new BotCommand(LEAVE_REVIEW, "Оставить отзыв"),
            new BotCommand(VIEW_MUSEUM, "Посмотреть информацию о музее"),
            new BotCommand(VIEW_MUSEUM_RANK, "Посмотреть рейтинг и отзывы музея")
    );

    /**
     * Получить меню команд
     * @return Список из команд, которые отображаются в меню
     */
    public List<BotCommand> getMenuOfCommands() {
        return commands;
    }

    /**
     * Создаёт графический интерфейс в виде кнопок с выбором
     * @param callbackData в зависимости от того, какая команда сейчас выполняется
     * @param variants варианты выбора для пользователя
     * @return виджет последовательной разметки кнопками
     */
    public InlineKeyboardMarkup getMarkupInline(String callbackData, Map<Long, String> variants){
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        for(Map.Entry<Long, String> variant : variants.entrySet()){
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(variant.getValue());
            inlineKeyboardButton.setCallbackData(callbackData + " " + variant.getKey());
            rowInline.add(inlineKeyboardButton);
            rowsInline.add(rowInline);
        }
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}
