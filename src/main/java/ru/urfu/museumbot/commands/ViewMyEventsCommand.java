package ru.urfu.museumbot.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.service.SendBotMessageService;
import ru.urfu.museumbot.jpa.service.ServiceContext;
import ru.urfu.museumbot.jpa.service.UserService;

import java.util.stream.Collectors;

/**
 * Класс для обработки команды просмотра мероприятий, на которые зарегистрирован пользователь
 */
public class ViewMyEventsCommand implements  Command {

    private final SendBotMessageService sendBotMessageService;
    private final UserService userService;

    public ViewMyEventsCommand(SendBotMessageService sendBotMessageService, ServiceContext serviceContext) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = serviceContext.getUserService();
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = viewMyEvents(chatId);
        if (text.length() == 0) {
            text = "Вы ещё не записаны ни на одно мероприятие";
        }
        sendBotMessageService.sendMessage(chatId.toString(), text);
    }

    /**
     * <p>Посмотреть предстоящие мероприятия пользователя</p>
     */
    private String viewMyEvents(Long chatId) {
        return userService
                .getUserEvents(chatId)
                .stream()
                .map(Event::toFormattedString)
                .collect(Collectors.joining("\n\n===============================\n\n"));
    }
}
