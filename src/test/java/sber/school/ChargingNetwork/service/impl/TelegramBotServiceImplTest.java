package sber.school.ChargingNetwork.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import sber.school.ChargingNetwork.config.BotConfig;
import sber.school.ChargingNetwork.model.station.Address;
import sber.school.ChargingNetwork.model.station.Station;
import sber.school.ChargingNetwork.model.user.Role;
import sber.school.ChargingNetwork.model.user.User;
import sber.school.ChargingNetwork.service.UserService;

import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramBotServiceImplTest {

    @Mock
    private BotConfig botConfig;
    @Mock
    private UserService userService;

    @InjectMocks
    private TelegramBotServiceImpl telegramBotService;

    @Test
    public void onUpdateReceived_startMessage() throws TelegramApiException {
        var update = mock(Update.class);
        var message = mock(Message.class);
        var sendMessage = new SendMessage();
        sendMessage.setChatId(1L);
        sendMessage.setText("Чтобы подписаться на уведомления станции, введите фамилию и имя через пробел." + "\n" +
                "Фамилия и имя должны соответствовать данным из профиля приложения." + "\n" +
                "Пользователь должен иметь право управления станциями." + "\n" +
                "Пример:" + "\n" +
                "Ломов Артем");

        doReturn(true).when(update).hasMessage();
        doReturn(message).when(update).getMessage();
        doReturn(true).when(message).hasText();
        doReturn("/start").when(message).getText();
        doReturn(1L).when(message).getChatId();

        telegramBotService.onUpdateReceived(update);

        verify(update, times(1)).hasMessage();
        verify(update, times(3)).getMessage();
        verify(message, times(1)).getText();
        verify(message, times(1)).getChatId();
    }

    @Test
    public void onUpdateReceived_helpMessage() throws TelegramApiException {
        var update = mock(Update.class);
        var message = mock(Message.class);
        var sendMessage = new SendMessage();
        sendMessage.setChatId(1L);
        sendMessage.setText("Чтобы подписаться на уведомления станции, введите фамилию и имя через пробел." + "\n" +
                "Фамилия и имя должны соответствовать данным из профиля приложения." + "\n" +
                "Пользователь должен иметь право управления станциями." + "\n" +
                "Пример:" + "\n" +
                "Ломов Артем");

        doReturn(true).when(update).hasMessage();
        doReturn(message).when(update).getMessage();
        doReturn(true).when(message).hasText();
        doReturn("/help").when(message).getText();
        doReturn(1L).when(message).getChatId();

        telegramBotService.onUpdateReceived(update);

        verify(update, times(1)).hasMessage();
        verify(update, times(3)).getMessage();
        verify(message, times(1)).getText();
        verify(message, times(1)).getChatId();
    }

    @Test
    public void onUpdateReceived_validUser() throws TelegramApiException {
        var update = mock(Update.class);
        var message = mock(Message.class);
        var sendMessage = new SendMessage();
        sendMessage.setChatId(1L);
        sendMessage.setText("Подписка успешно добавлена");
        var user = new User();
        user.setUserId(2);
        user.setRoles(Set.of(new Role(1, "ROLE_MANAGER_STATION")));

        doReturn(true).when(update).hasMessage();
        doReturn(message).when(update).getMessage();
        doReturn(true).when(message).hasText();
        doReturn("Ломов Артем").when(message).getText();
        doReturn(1L).when(message).getChatId();
        doReturn(user).when(userService).getUserByFirstNameAndLastName("Артем", "Ломов");

        telegramBotService.onUpdateReceived(update);

        verify(update, times(1)).hasMessage();
        verify(update, times(3)).getMessage();
        verify(message, times(1)).getText();
        verify(message, times(1)).getChatId();
        verify(userService, times(1)).getUserByFirstNameAndLastName("Артем", "Ломов");
        verify(userService, times(1)).updateUser(2, user);
    }

    @Test
    public void onUpdateReceived_validUserWithoutRoleManagerStation() throws TelegramApiException {
        var update = mock(Update.class);
        var message = mock(Message.class);
        var sendMessage = new SendMessage();
        sendMessage.setChatId(1L);
        sendMessage.setText("Подписка успешно добавлена");
        var user = new User();
        user.setUserId(2);
        user.setRoles(Set.of(new Role(1, "ROLE_MANAGER_USER")));

        doReturn(true).when(update).hasMessage();
        doReturn(message).when(update).getMessage();
        doReturn(true).when(message).hasText();
        doReturn("Ломов Артем").when(message).getText();
        doReturn(1L).when(message).getChatId();
        doReturn(user).when(userService).getUserByFirstNameAndLastName("Артем", "Ломов");

        telegramBotService.onUpdateReceived(update);

        verify(update, times(1)).hasMessage();
        verify(update, times(3)).getMessage();
        verify(message, times(1)).getText();
        verify(message, times(1)).getChatId();
        verify(userService, times(1)).getUserByFirstNameAndLastName("Артем", "Ломов");
        verify(userService, never()).updateUser(anyInt(), any(User.class));
    }

    @Test
    public void onUpdateReceived_invalidUser() throws TelegramApiException {
        var update = mock(Update.class);
        var message = mock(Message.class);
        var sendMessage = new SendMessage();
        sendMessage.setChatId(1L);
        sendMessage.setText("Подписка успешно добавлена");

        doReturn(true).when(update).hasMessage();
        doReturn(message).when(update).getMessage();
        doReturn(true).when(message).hasText();
        doReturn("Ломов Артем").when(message).getText();
        doReturn(1L).when(message).getChatId();
        doThrow(NoSuchElementException.class).when(userService).getUserByFirstNameAndLastName("Артем", "Ломов");

        telegramBotService.onUpdateReceived(update);

        verify(update, times(1)).hasMessage();
        verify(update, times(3)).getMessage();
        verify(message, times(1)).getText();
        verify(message, times(1)).getChatId();
        verify(userService, times(1)).getUserByFirstNameAndLastName("Артем", "Ломов");
        verify(userService, never()).updateUser(anyInt(), any(User.class));
    }

    @Test
    public void sendStationError_validUser() {
        var station = mock(Station.class);
        var manager = mock(User.class);
        var address = mock(Address.class);

        doReturn(manager).when(station).getManager();
        doReturn(1L).when(manager).getChatId();
        doReturn("ЭЗС1").when(station).getStationName();
        doReturn(address).when(station).getAddress();
        doReturn("г. Рязань, ул. Дубовая, 1").when(address).toString();

        telegramBotService.sendStationError(station);

        verify(station, times(1)).getManager();
        verify(manager, times(2)).getChatId();
        verify(station, times(1)).getStationName();
        verify(station, times(1)).getAddress();
    }

    @Test
    public void sendStationError_validUserWithoutChatId() {
        var station = mock(Station.class);
        var manager = mock(User.class);

        doReturn(manager).when(station).getManager();
        doReturn(null).when(manager).getChatId();

        telegramBotService.sendStationError(station);

        verify(station, times(1)).getManager();
        verify(manager, times(1)).getChatId();
        verify(station, never()).getStationName();
        verify(station, never()).getAddress();
    }

    @Test
    public void sendStationError_stationWithoutManager() {
        var station = mock(Station.class);
        var manager = mock(User.class);

        doReturn(manager).when(station).getManager();
        doThrow(NullPointerException.class).when(manager).getChatId();

        telegramBotService.sendStationError(station);

        verify(station, times(1)).getManager();
        verify(manager, times(1)).getChatId();
        verify(station, never()).getStationName();
        verify(station, never()).getAddress();
    }

    @Test
    public void getBootUsername() {
        System.out.println(telegramBotService.getBotUsername());
    }
}