package ru.urfu.museumbot.commands;

/**
 * Класс состояния пользователя или команды
 */
public enum State {
    INIT("Init"),
    RATE("Rate"),
    RATE_PREV("Rate_prev"),
    COMMENT("Comment");
    private final String stateString;
    State(String stateString) {
        this.stateString = stateString;
    }
    public String getStateString() {
        return stateString;
    }
}
