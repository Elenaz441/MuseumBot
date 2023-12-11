package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.buttons.ButtonsContext;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.Exhibit;
import ru.urfu.museumbot.jpa.service.ExhibitService;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.urfu.museumbot.commands.Commands.VIEW_EXHIBIT;

/**
 * Промежуточная команда перед просмотром информации об экспонате.
 * Здесь пользователю предоставляется список экспонатов.
 */
@Service
public class PreViewExhibitCommand implements Command{
    private final UserService userService;
    static final String EXHIBIT_NOT_AVAILABLE = "Выставка ещё не началась. Эта команда недоступна";
    static final String CALLBACK_DATA = "ViewExhibit";
    static final String EXHIBIT_IS_AVAILABLE = "Выберите экспонат:";
    static  final String IF_NO_EXHIBIT_BY_MUSEUM = "Извините," +
            " в базе данных отсутствует информация об экспонатах данного музея";
    private final ExhibitService exhibitService;

    @Autowired
    public PreViewExhibitCommand(UserService userService, ExhibitService exhibitService) {
        this.exhibitService = exhibitService;
        this.userService = userService;
    }

    @Override
    public Message getMessage(CommandArgs args) {
        Long chatId = args.getChatId();

        List<Event> currentEvent = userService.getUserEventsAfterNow(chatId);
        if(currentEvent.isEmpty()){
            return new Message(chatId, EXHIBIT_NOT_AVAILABLE);
        }
        List<Exhibit> exhibits = exhibitService.getMuseumExhibits(currentEvent.get(0).getId());
        if (exhibits.isEmpty()){
            return new Message(chatId, IF_NO_EXHIBIT_BY_MUSEUM);
        }
        Map<Long, String> variants = exhibits
                .stream().collect(Collectors.toMap(Exhibit::getId, Exhibit::getTitle));
        Message message = new Message(chatId, EXHIBIT_IS_AVAILABLE);
        message.setButtonsContext(new ButtonsContext(CALLBACK_DATA, variants));
        return message;
    }

    @Override
    public String getCommandName() {
        return VIEW_EXHIBIT;
    }

}
