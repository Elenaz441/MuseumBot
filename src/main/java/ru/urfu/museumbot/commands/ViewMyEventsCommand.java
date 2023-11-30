package ru.urfu.museumbot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.urfu.museumbot.dataFormat.EventFormat;
import ru.urfu.museumbot.jpa.service.UserService;

import java.util.stream.Collectors;

/**
 * Класс для обработки команды просмотра мероприятий, на которые зарегистрирован пользователь
 */
public class ViewMyEventsCommand implements  Command {

    private final UserService userService;

    public ViewMyEventsCommand(UserService userService) {
        this.userService = userService;
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public SendMessage getMessage(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = viewMyEvents(chatId);
        if (text.length() == 0) {
            text = "Вы ещё не записаны ни на одно мероприятие";
        }
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        return message;
    }

    /**
     * <p>Посмотреть предстоящие мероприятия пользователя</p>
     */
    private String viewMyEvents(Long chatId) {
        EventFormat eventFormat = new EventFormat();
        return userService
                .getUserEvents(chatId)
                .stream()
                .map(eventFormat::toFormattedString)
                .collect(Collectors.joining("\n\n===============================\n\n"));
    }
}
