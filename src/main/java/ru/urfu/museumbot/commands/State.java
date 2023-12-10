package ru.urfu.museumbot.commands;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum State {
    INIT("Init"),
    RATE("Rate"),
    RATE_PREV("Rate_prev"),
    COMMENT("Comment");
    private static final Map<String, State> ENUM_MAP;

    static {
        Map<String,State> map = new HashMap<>();
        for (State instance : State.values()) {
            map.put(instance.getStateString(),instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static State get (String stateString) {
        return ENUM_MAP.get(stateString);
    }
    //    SET_DELAY(stateString),
//    NOTIFY(stateString);
    private String stateString;
    State(String stateString) {
        this.stateString = stateString;
    }
    public String getStateString() {
        return stateString;
    }
}
