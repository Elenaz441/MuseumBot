package ru.urfu.museumbot.models;

import java.util.Objects;

/**
 * <p>Модель таблицы "Пользователь"</p>
 * <p>{@link User#id} уникальный идентификатор пользователя</p>
 * <p>{@link User#name} имя</p>
 * <p>{@link User#settingReminders} флаг на разрешение пользователя присылать напоминание о предстоящем мероприятии</p>
 * <p>{@link User#randomExhibitSetting} флаг на разешение расслыки о случайном экспонате</p>
 *
 */
public class User {
    private Long id;
    private String name;

    private boolean settingReminders;
    private boolean randomExhibitSetting;

    public User(Long id, String name, boolean settingReminders, boolean randomExhibitSetting) {
        this.id = id;
        this.name = name;
        this.settingReminders = settingReminders;
        this.randomExhibitSetting = randomExhibitSetting;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSettingReminders() {
        return settingReminders;
    }

    public boolean isRandomExhibitSetting() {
        return randomExhibitSetting;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", settingReminders=" + settingReminders +
                ", randomExposureSetting=" + randomExhibitSetting +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return settingReminders == user.settingReminders && randomExhibitSetting == user.randomExhibitSetting && Objects.equals(id, user.id) && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, settingReminders, randomExhibitSetting);
    }
}
