package ru.urfu.museumbot.dataFormat;

import ru.urfu.museumbot.enums.DayOfWeek;
import ru.urfu.museumbot.jpa.models.User;

import java.text.SimpleDateFormat;

/**
 * Класс форматирования для вывода пользователю информации о настройках
 */
public class UserFormat {

    /**
     * Метод для преобразования информации о настройках пользователя в строку
     */
    public String convertUserSettingsToString(User user) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String settingReminders = user.isSettingReminders() ? "да" : "нет";
        String randomExposureSetting = user.isRandomExposureSetting() ? "да" : "нет";
        String result = String.format("""
                        У вас следующие настройки:
                        Присылать ли вам напоминание о мероприятии, на которое вы записаны, за день до начала? - %s
                        Присылать ли вам информацию о случайном экспонате? - %s
                        """,
                settingReminders,
                randomExposureSetting);
        if (user.isRandomExposureSetting()) {
            result += String.format("""
                            В какой день недели присылать информацию об экспонате? - %s
                            Присылать уведомления в %s.""",
                    DayOfWeek.values()[user.getDayOfWeekDistribution()].getDayString(),
                    dateFormat.format(user.getNotificationTime()));
        } else if (user.isSettingReminders()) {
            result += String.format("Присылать уведомления в %s.", dateFormat.format(user.getNotificationTime()));
        }
        return result;
    }

}
