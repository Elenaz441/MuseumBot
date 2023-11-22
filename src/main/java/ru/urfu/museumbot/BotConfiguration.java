package ru.urfu.museumbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.urfu.museumbot.jpa.service.ServiceContext;


/**
 * <p>Класс инициализирующий телеграм бота</p>
 */
@Configuration
public class BotConfiguration {

    /**
     * <p> Создаёт сессию и регистрирует телеграм бота</p>
     */
    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramBot bot) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(bot);
        return api;
    }

    /**
     * Создаёт телеграм-бота
     */
    @Bean
    public TelegramBot telegramBot(@Value("${bot.name}") String botName,
                                   @Value("${bot.token}") String botToken,
                                   ServiceContext serviceContext) {
        return new TelegramBot(botToken, botName, serviceContext);
    }

}
