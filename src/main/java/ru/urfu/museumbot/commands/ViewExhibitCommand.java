package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.jpa.service.ExhibitService;
import ru.urfu.museumbot.message.Message;

/**
 * Класс для просмотра названий всех экспонатов музея
 */
@Service
public class ViewExhibitCommand implements Command {

    private static final String COMMAND_NAME = "ViewExhibit";

    private final ExhibitService exhibitService;

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
        return exhibitService.getFormattedString(exhibitService.getExhibitById(exhibitId));
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

}
