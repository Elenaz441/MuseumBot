package ru.urfu.museumbot.message;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import ru.urfu.museumbot.buttons.ButtonsContext;

import java.util.List;
import java.util.Optional;

import static ru.urfu.museumbot.commands.Commands.*;
import static ru.urfu.museumbot.commands.Commands.VIEW_MY_EVENTS;

/**
 * класс сообщения не привязанного к реализации платформы
 */
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

    /**
     * содержит все реализованные команды для удобного пользования ботом
     * @return команды и их описание для меню в боте
     */
    public List<BotCommand> getMenuOfCommands() {
        return commands;
    }

    /**
     * текст сообщения
     */
    private String text = "";
    /**
     * идентификатор пользователя, которому оно отправляется
     */
    private Long chatId = null;
    /**
     * Некоторые сообщения отправляются с разметкой кнопок
     * контекст для создания подобной разметки
     */
    private Optional<ButtonsContext> buttonsContext = Optional.empty();

    public Long getChatId() {
        return chatId;
    }

    public String getText() {
        return text;
    }

    public Optional<ButtonsContext> getButtonsContext() {
        return buttonsContext;
    }

    public void setButtonsContext(ButtonsContext buttons) {
        this.buttonsContext = Optional.of(buttons);
    }

    public Message(Long chatId, String text) {
        this.chatId = chatId;
        this.text = text;
    }
    public Message() {
    }
}
