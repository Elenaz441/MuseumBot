package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.buttons.ButtonsContext;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.Exhibit;
import ru.urfu.museumbot.jpa.service.EventService;
import ru.urfu.museumbot.jpa.service.ExhibitService;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.urfu.museumbot.commands.Commands.VIEW_EXHIBIT;

@Service
public class PreViewExhibitCommand implements Command{
    private final UserService userService;

    private final String EXHIBIT_NOT_AVAIBLE = "Выставка ещё не началась. Эта команда недоступна";
    private final String CALLBACK_DATA = "ViewExhibit";
    private final String EXHIBIT_IS_AVAIBLE = "Выберите экспонат:";
    private final ExhibitService exhibitService;
    private Event currentEvent;

    @Autowired
    public PreViewExhibitCommand(UserService userService, ExhibitService exhibitService) {
        this.exhibitService = exhibitService;
        this.userService = userService;
    }

    @Override
    public Message getMessage(CommandArgs args) {
        List<Event> usersEvents = userService
                .getUserEvents(args.getChatId())
                .stream().collect(Collectors.toList());
        boolean isUserAtEvent = isUserAtEvent(usersEvents);
        if(!isUserAtEvent){
            return new Message(args.getChatId(), EXHIBIT_NOT_AVAIBLE);
        }
        Map<Long, String> variants = exhibitService.getMuseumExhibits(currentEvent.getId())
                .stream().collect(Collectors.toMap(Exhibit::getId, Exhibit::getTitle));
        Message message = new Message(args.getChatId(), EXHIBIT_IS_AVAIBLE);
        message.setButtonsContext(new ButtonsContext(CALLBACK_DATA, variants));
        return message;
    }

    @Override
    public String getCommandName() {
        return VIEW_EXHIBIT;
    }
    private boolean isUserAtEvent(List<Event> usersEvents) {
      Instant now = Instant.now();
        for(Event event: usersEvents){
            Instant eventDate = event.getDate().toInstant();
            if(now.isAfter(eventDate) && eventDate.plus(event.getDuration(), ChronoUnit.MINUTES).isAfter(now)) {
                this.currentEvent = event;
                return true;
            }
        }
        return false;
    }
}
