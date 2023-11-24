package ru.urfu.museumbot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.urfu.museumbot.TelegramBot;
import ru.urfu.museumbot.buttons.ButtonsContent;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.service.SendBotMessageService;

import java.util.ArrayList;
import java.util.List;

/**
 * Отправка сообщений для тестирования
 */
public class FakeSender extends SendBotMessageService {

    private final List<SendMessage> messages = new ArrayList<>();
    private final ButtonsContent buttonsContent;

    public List<SendMessage> getMessages() {
        return messages;
    }

    public FakeSender(TelegramBot telegramBot) {
        super(telegramBot);
        this.buttonsContent = new ButtonsContent();
    }

    @Override
    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        messages.add(sendMessage);
    }

    @Override
    public void sendMessageWithButtons(String chatId, String message, String callbackData, List<Event> events) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        InlineKeyboardMarkup markupInline = buttonsContent.getMarkupInline(callbackData, events);
        sendMessage.setReplyMarkup(markupInline);
        messages.add(sendMessage);
    }
}
