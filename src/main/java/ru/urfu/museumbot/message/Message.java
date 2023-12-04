package ru.urfu.museumbot.message;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import ru.urfu.museumbot.buttons.MarkupButtonsTelegram;

import java.util.List;
import java.util.Optional;

import static ru.urfu.museumbot.commands.Commands.*;
import static ru.urfu.museumbot.commands.Commands.VIEW_MY_EVENTS;

public class Message {
    /**
     * Отображение, которое содержит ключ:команда значение:описание
     */
    private final List<BotCommand> commands = List.of(
            new BotCommand(START, "Старт"),
            new BotCommand(HELP, "Нужна помощь?"),
            new BotCommand(VIEW_UPCOMING_EVENTS, "Посмотреть ближайшие мероприятия"),
            new BotCommand(SIGN_UP_FOR_EVENT, "Зарегистрироваться на мероприятие"),
            new BotCommand(CANCEL, "Отменить запись на мероприятие"),
            new BotCommand(VIEW_MY_EVENTS, "Посмотреть на записанные мероприятия")
    );
    public List<BotCommand> getMenuOfCommands() {
        return commands;
    }
    private String text = "";
    private Long chatId = null;
    private Optional<MarkupButtonsTelegram> buttons = Optional.empty();

    public Long getChatId() {
        return chatId;
    }

    public String getText() {
        return text;
    }

    public Optional<MarkupButtonsTelegram> getButtons() {
        return buttons;
    }

    public void setButtons(MarkupButtonsTelegram buttons) {
        this.buttons = Optional.of(buttons);
    }

    public Message(Long chatId, String text) {
        this.chatId = chatId;
        this.text = text;
    }
    public Message() {
    }
}
