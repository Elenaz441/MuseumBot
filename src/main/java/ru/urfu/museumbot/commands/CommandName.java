//package ru.urfu.museumbot.commands;
//
///**
// * Перечисление команд
// */
//public enum CommandName {
//    START("/start"),
//    HELP("/help"),
//    VIEW_UPCOMING_EVENTS("/view_upcoming_events"),
//    SIGN_UP_FOR_EVENT("/sign_up_for_event"),
//    CANCEL("/cancel"),
//    VIEW_MY_EVENTS("/view_my_events");
//    private final String commandName;
//
//
//    public String getCommandName() {
//        return commandName;
//    }
//
//
//
//    CommandName(String name) {
//        this.commandName = name;
//    }
//
//}
package ru.urfu.museumbot.commands;

/**
 */
public enum CommandName {

    START("/start"),
    HELP("/help"),

    VIEW_UPCOMING_EVENTS("/view_upcoming_events"),
    SIGN_UP_FOR_EVENT("/sign_up_for_event"),
    CANCEL("/cancel"),
    VIEW_MY_EVENTS("/view_my_events");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
