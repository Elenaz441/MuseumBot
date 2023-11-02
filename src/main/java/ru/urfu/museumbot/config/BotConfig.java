package ru.urfu.museumbot.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * <p>Класс Конфигурации телеграм бота</p>
 * <p>{@link BotConfig#botName} имя бота</p>
 * <p>{@link BotConfig#token} токен для бота</p>
 */
@Configuration
@PropertySource("application.properties")
public class BotConfig {

    @Value("${bot.name}")
    String botName;

    @Value("${bot.token}")
    String token;

    public String getBotName() {
        return botName;
    }

    public String getToken() {
        return token;
    }
}
