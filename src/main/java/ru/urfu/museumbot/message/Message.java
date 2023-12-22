package ru.urfu.museumbot.message;

import ru.urfu.museumbot.buttons.ButtonsContext;

import java.util.Objects;
import java.util.Optional;

/**
 * класс сообщения не привязанного к реализации платформы
 */
public class Message {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(text, message.text) && Objects.equals(chatId, message.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, chatId);
    }
}
