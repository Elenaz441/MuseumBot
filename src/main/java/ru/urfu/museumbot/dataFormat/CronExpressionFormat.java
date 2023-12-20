package ru.urfu.museumbot.dataFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс для формирования СronTrigegr выражения
 * @see org.springframework.scheduling.support.CronTrigger СronTrigegr
 */
public class CronExpressionFormat {
    /**
     * @param notificationTime время в формате часы:минуты
     * @param dayOfWeek день недели
     * @return СronTrigegr выражение, которое устанавливает шаблон на еженедельное событие
     */
    public String getCronExpressionByUserSettings(Date notificationTime, Integer dayOfWeek) {
        return String.format("0 %s %s * * %d", new SimpleDateFormat("mm").format(notificationTime),
                new SimpleDateFormat("HH").format(notificationTime), dayOfWeek);
    }
}
