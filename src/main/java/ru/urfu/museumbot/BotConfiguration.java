package ru.urfu.museumbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.urfu.museumbot.commands.BotLogic;
import ru.urfu.museumbot.commands.TelegramBot;


/**
 * <p>Класс инициализирующий телеграм бота</p>
 */
@Configuration
public class BotConfiguration {

    /**
     * <p> Создаёт сессию и регистрирует телеграм бота</p>
     */
    @Bean
    public TelegramBotsApi telegramBotsApi(BotLogic logic,
                                           @Value("${bot.name}") String botName,
                                           @Value("${bot.token}") String botToken)
            throws TelegramApiException {
        TelegramBot bot = new TelegramBot(logic, botToken, botName);
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(bot);
        return api;
    }

}
