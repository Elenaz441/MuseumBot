package ru.urfu.museumbot.commands;


/**
 * <p>Класс для хранения статических текстов</p>
 */
public class StaticText {

    /**
     * <p>Текст для запроса /help</p>
     */
    static String HELP_TEXT = """
            Доступны следующие команды:
            \t/help - Справка
            \t/view_upcoming_events - Посмотреть предстоящие мероприятия
            \t/sign_up_for_event - Зарегистрироваться на мероприятие.
            \t/cancel - Отменить запись на мероприятие.
            \t/view_my_events - Посмотреть список мероприятий, на которые вы записаны.
            """;

}
