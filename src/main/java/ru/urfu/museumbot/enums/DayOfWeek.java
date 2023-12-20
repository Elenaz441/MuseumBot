package ru.urfu.museumbot.enums;

/**
 * Перечисление дней недели
 */
public enum DayOfWeek {
    SUNDAY("Воскресенье"),
    MONDAY("Понедельник"),
    TUESDAY("Вторник"),
    WEDNESDAY("Среда"),
    THURSDAY("Четверг"),
    FRIDAY("Пятница"),
    SATURDAY("Суббота");

    private final String dayString;

    DayOfWeek(String dayString) {
        this.dayString = dayString;
    }

    public String getDayString() {
        return dayString;
    }

}
