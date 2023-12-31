package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.dataFormat.EventFormat;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

import java.util.stream.Collectors;

import static ru.urfu.museumbot.commands.Commands.VIEW_MY_EVENTS;

/**
 * Класс для обработки команды просмотра мероприятий, на которые зарегистрирован пользователь
 */
@Service
public class ViewMyEventsCommand implements Command {

    private final UserService userService;

    @Autowired
    public ViewMyEventsCommand(UserService userService) {
        this.userService = userService;
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public Message getMessage(CommandArgs args) {
        Long chatId = args.getChatId();
        String text = viewMyEvents(chatId);
        if (text.isEmpty()) {
            text = "Вы ещё не записаны ни на одно мероприятие";
        }
        return new Message(chatId, text);
    }

    @Override
    public String getCommandName() {
        return VIEW_MY_EVENTS;
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
