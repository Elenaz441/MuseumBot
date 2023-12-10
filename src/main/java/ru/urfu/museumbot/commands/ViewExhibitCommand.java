package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.commands.Command;
import ru.urfu.museumbot.commands.CommandArgs;
import ru.urfu.museumbot.commands.State;
import ru.urfu.museumbot.dataFormat.ExhibitFormat;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.service.ExhibitService;
import ru.urfu.museumbot.message.Message;

import java.util.Map;
import java.util.stream.Collectors;
@Service
public class ViewExhibitCommand implements Command {
    private final ExhibitService exhibitService;

    static final String COMMAND_NAME = "ViewExhibit";

    @Autowired
    public ViewExhibitCommand(ExhibitService exhibitService) {
        this.exhibitService = exhibitService;
    }


    @Override
    public Message getMessage(CommandArgs args) {
        Long chatId = args.getChatId();
        String callback = args.getCallbackData();
        return new Message(chatId, viewExhibit(Long.valueOf(callback.split(" ")[1])));
    }

    private String viewExhibit(Long exhibitId) {
        return new ExhibitFormat().toFormattedString(exhibitService.getExhibitById(exhibitId));
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

}