package sber.school.ChargingNetwork.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import sber.school.ChargingNetwork.config.BotConfig;
import sber.school.ChargingNetwork.model.station.Station;
import sber.school.ChargingNetwork.model.user.User;
import sber.school.ChargingNetwork.service.TelegramBotService;
import sber.school.ChargingNetwork.service.UserService;

import java.util.NoSuchElementException;

@Service
public class TelegramBotServiceImpl extends TelegramLongPollingBot implements TelegramBotService {

    private static final String HELP_MESSAGE =
            "Чтобы подписаться на уведомления станции, введите фамилию и имя через пробел." + "\n" +
                    "Фамилия и имя должны соответствовать данным из профиля приложения." + "\n" +
                    "Пользователь должен иметь право управления станциями." + "\n" +
                    "Пример:" + "\n" +
                    "Ломов Артем";

    private static final String SUBSCRIPTION_ADDED =
            "Подписка успешно добавлена";

    private static final String ERROR_ADDING_SUBSCRIPTION =
            "Подписка не добавлена. Введите /help для получения информации";


    private final BotConfig botConfig;
    private final UserService userService;

    public TelegramBotServiceImpl(BotConfig botConfig, UserService userService) {
        super(botConfig.getToken());
        this.botConfig = botConfig;
        this.userService = userService;
    }

    /**
     *
     * Метод, обрабатывающий входящие сообщения в чате телеграм-бота
     *
     * @param update - Параметр, содержащий информацию о событии чата, в том числе и тело сообщения
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var message = update.getMessage().getText();
            var chatId = update.getMessage().getChatId();
            switch (message) {
                case "/start":
                case "/help":
                    sendMessage(chatId, HELP_MESSAGE);
                    break;
                default:
                    if (addNewManagerToBot(message, chatId)) {
                        sendMessage(chatId, SUBSCRIPTION_ADDED);
                    } else {
                        sendMessage(chatId, ERROR_ADDING_SUBSCRIPTION);
                    }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public void sendStationError(Station station) {
        try {
            User manager = station.getManager();
            if (manager.getChatId() != null) {
                sendMessage(manager.getChatId(),
                        String.format("Неисправность зарядной станции %s по адресу %s",
                                station.getStationName(),
                                station.getAddress()));
            }
        } catch (NullPointerException ignored) {}
    }

    /**
     *
     * Метод id данного чата определенному пользователю в базу данных. По данному id приложение определяет, в какой чат
     * отправлять уведомления о неисправности
     *
     * @param firstNameAndLastName - Имя и фамилия пользователя
     * @param chatId - ID используемого чата
     * @return - Результат добавления подписки: true - подписка добавлена, false - подписка не добавлена
     */
    private boolean addNewManagerToBot(String firstNameAndLastName, Long chatId) {
        try {
            var managerData = firstNameAndLastName.split(" ");
            if (managerData.length == 2) {
                var user = userService.getUserByFirstNameAndLastName(managerData[1], managerData[0]);
                var hasRole = user.getRoles().stream()
                        .anyMatch(role -> "ROLE_MANAGER_STATION".equals(role.getRoleName()));
                if (hasRole) {
                    user.setChatId(chatId);
                    userService.updateUser(user.getUserId(), user);
                    return true;
                }
            }
        } catch (NoSuchElementException ignored) {}
        return false;
    }

    private void sendMessage(Long chatId, String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException ignored) {}
    }

}
