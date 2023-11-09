package ru.urfu.museumbot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.urfu.museumbot.commands.TelegramBot;


/**
 * <p>Класс инициализирующий телеграм бота</p>
 */
@Configuration
public class BotConfig {

    /**
     * <p> Создаёт сессию и регистрирует телеграм бота</p>
     */
    @Bean
    public TelegramBot registration(TelegramBot bot){
        try {
            new TelegramBotsApi(DefaultBotSession.class).registerBot(bot);
            System.out.println("Бот @"+bot.getBotUsername()+" успешно запущен!!!");
        } catch (Exception e){
            e.printStackTrace();
        }
        return bot;
    }

}
