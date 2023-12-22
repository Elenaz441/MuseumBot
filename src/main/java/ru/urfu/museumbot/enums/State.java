package ru.urfu.museumbot.enums;

/**
 * Перечисление состояния пользователя
 */
public enum State {
    INIT("Init"),
    RATE("Rate"),
    RATE_PREV("Rate_prev"),
    COMMENT("Comment"),
    SET_NOTIFICATION("Set_notification"),
    SET_DISTRIBUTION("Set_distribution"),
    SET_TIME("Set_time"),
    SET_DAY_OF_WEEK("Set_day_of_week");

    private final String stateString;

    State(String stateString) {
        this.stateString = stateString;
    }

    public String getStateString() {
        return stateString;
    }
}
