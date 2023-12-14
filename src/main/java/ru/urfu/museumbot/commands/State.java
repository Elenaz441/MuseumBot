package ru.urfu.museumbot.commands;
import java.util.Arrays;

public enum State {
    INIT("Init"),
    RATE("Rate"),
    RATE_PREV("Rate_prev"),
    COMMENT("Comment");

    /**
     * Получить состояние по идентификатору
     */
    public static State get(String stateString)
    {
        return Arrays.stream(State.values())
                .filter(state -> state.getStateString().equals(stateString))
                .findFirst().orElse(INIT);
    }

    private final String stateString;
    State(String stateString) {
        this.stateString = stateString;
    }
    public String getStateString() {
        return stateString;
    }
}
