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
            \t/sign_up_for_event <EventId> - Зарегистрироваться на мероприятие по его идентификационному номеру. Этот номер можно узнать из функции /view_upcoming_events.
            \t/cancel <EventId> - Отменить запись на мероприятие.
            \t/view_my_events - Посмотреть список мероприятий, на которые вы записаны.
            """;

}
