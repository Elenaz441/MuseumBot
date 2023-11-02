package ru.urfu.museumbot.models;

import java.util.Objects;

/**
 * <p>Модель таблицы "Пользователь"</p>
 * <p>{@link User#id} уникальный идентификатор пользователя</p>
 * <p>{@link User#name} имя</p>
 * <p>{@link User#settingReminders} флаг на разрешение пользователя присылать напоминание о предстоящем мероприятии</p>
 * <p>{@link User#randomExposureSetting} флаг на разешение расслыки о случайном экспонате</p>
 *
 */
public class User {
    private Long id;
    private String name;

    private boolean settingReminders;
    private boolean randomExposureSetting;

    public User(Long id, String name, boolean settingReminders, boolean randomExposureSetting) {
        this.id = id;
        this.name = name;
        this.settingReminders = settingReminders;
        this.randomExposureSetting = randomExposureSetting;
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

    public boolean isRandomExposureSetting() {
        return randomExposureSetting;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", settingReminders=" + settingReminders +
                ", randomExposureSetting=" + randomExposureSetting +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return settingReminders == user.settingReminders && randomExposureSetting == user.randomExposureSetting && Objects.equals(id, user.id) && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, settingReminders, randomExposureSetting);
    }
}
