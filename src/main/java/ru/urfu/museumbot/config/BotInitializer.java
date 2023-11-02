package ru.urfu.museumbot.config;

import ru.urfu.museumbot.service.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * <p>Класс инициализирующий телеграм бота</p>
 * <p>{@link BotInitializer#bot} Экземпляр телеграм бота, в котором реализован основной функционал</p>
 */
@Component
public class BotInitializer {

    @Autowired
    TelegramBot bot;


    /**
     *
     * <p> Создаёт сессию и регистрирует телеграм бота</p>
     * @throws TelegramApiException базовый класс исключений при неудачной попытке регистрации бота
     */
    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(bot);
        }
        catch (TelegramApiException e) {

        }
    }
}
