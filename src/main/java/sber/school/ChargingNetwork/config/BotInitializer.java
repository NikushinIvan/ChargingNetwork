package sber.school.ChargingNetwork.config;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import sber.school.ChargingNetwork.service.impl.TelegramBotServiceImpl;

/**
 *
 * Класс инициализауии Телеграм-бота
 *
 */
@Component
public class BotInitializer {

    private final TelegramBotServiceImpl telegramBot;

    public BotInitializer(TelegramBotServiceImpl telegramBot) {
        this.telegramBot = telegramBot;
    }

    /**
     *
     * Метод инициализации Телеграм-бота. Выполняется при инициализации или обновлении контекста приложения.
     * Регистрирует сервис бота в TelegramBotsApi
     *
     */
    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try {
            var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
