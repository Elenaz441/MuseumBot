package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.dataFormat.MuseumFormat;
import ru.urfu.museumbot.jpa.models.Museum;
import ru.urfu.museumbot.jpa.service.MuseumService;
import ru.urfu.museumbot.message.Message;

import static ru.urfu.museumbot.commands.Commands.GET_MUSEUM;

/**
 * Класс для просмотра информации о музее
 */
@Service
public class ViewMuseumCommand implements Command {

    private final MuseumService museumService;

    @Autowired
    public ViewMuseumCommand(MuseumService museumService) {
        this.museumService = museumService;
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public Message getMessage(CommandArgs args) {
        return new Message(
                args.getChatId(),
                viewMuseum(args.getCallbackData()));
    }

    @Override
    public String getCommandName() {
        return GET_MUSEUM;
    }

    /**
     * <p>Посмотреть информацию о музее</p>
     */
    private String viewMuseum(String callbackData) {
        MuseumFormat museumFormat = new MuseumFormat();
        Long museumId = Long.valueOf(callbackData.replace(GET_MUSEUM + " ", ""));
        Museum museum = museumService.getMuseumById(museumId);
        return museumFormat.toFormattedString(museum);
    }
}
