package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.buttons.ButtonsContext;
import ru.urfu.museumbot.jpa.models.Museum;
import ru.urfu.museumbot.jpa.service.MuseumService;
import ru.urfu.museumbot.message.Message;

import java.util.Map;
import java.util.stream.Collectors;

import static ru.urfu.museumbot.commands.Commands.GET_RANK;
import static ru.urfu.museumbot.commands.Commands.VIEW_MUSEUM_RANK;

/**
 * Промежуточная команда перед просмотром рейтинга и отзывов о музее.
 * Здесь пользователю предоставляется список музеев.
 */
@Service
public class PreViewMuseumsRankCommand implements Command {
    private static final String CHOOSE_MUSEUM_MESSAGE = "Выберете музей:";

    private final MuseumService museumService;

    @Autowired
    public PreViewMuseumsRankCommand(MuseumService museumService) {
        this.museumService = museumService;
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public Message getMessage(CommandArgs args) {
        Long chatId = args.getChatId();
        Message message = new Message(chatId, CHOOSE_MUSEUM_MESSAGE);
        Map<Long, String> variants = museumService.getMuseums()
                .stream()
                .collect(Collectors.toMap(Museum::getId, Museum::getTitle));
        message.setButtonsContext(new ButtonsContext(GET_RANK, variants));
        return message;
    }

    @Override
    public String getCommandName() {
        return VIEW_MUSEUM_RANK;
    }
}
