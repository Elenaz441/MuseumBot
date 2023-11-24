package ru.urfu.museumbot.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.urfu.museumbot.TelegramBot;
import ru.urfu.museumbot.buttons.ButtonsContent;
import ru.urfu.museumbot.jpa.models.Event;

import java.util.List;

/**
 * Сервис для отправки сообщений
 */
@Service
public class SendBotMessageService {

    private final TelegramBot bot;
    private final ButtonsContent buttonsContent;

    @Autowired
    public SendBotMessageService(TelegramBot telegramBot) {
        this.bot = telegramBot;
        this.buttonsContent = new ButtonsContent();
    }

    /**
     * Отправить новое сообщение
     * @param chatId - чат для отправки
     * @param message - сообщение
     */
    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.printf("Сообщение с текстом \"%s\" не отправилось!", message);
        }
    }

    /**
     * Отправить сообщение с кнопками
     * @param chatId - чат для отправки
     * @param message - сообщение
     * @param callbackData - тип кнопок
     * @param events - список мероприятий
     */
    public void sendMessageWithButtons(String chatId, String message, String callbackData, List<Event> events) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        InlineKeyboardMarkup markupInline = buttonsContent.getMarkupInline(callbackData, events);
        sendMessage.setReplyMarkup(markupInline);

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.printf("Сообщение с текстом \"%s\" не отправилось!", message);
        }
    }
}
