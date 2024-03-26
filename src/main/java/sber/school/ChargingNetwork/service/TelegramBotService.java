package sber.school.ChargingNetwork.service;

import sber.school.ChargingNetwork.model.station.Station;

/**
 *
 * Интерфейс содержит методы, необходимые для необходимые для реализации основного функционала Телеграм-бота. Представляет слой бизнес-логики
 *
 */
public interface TelegramBotService {

    /**
     *
     * Метод отправляет в чат диспетчеру станции (если он задан и подключен к телеграм-боту) сообщение об ошибке.
     *
     * @param station Объект станции, на которой возникла ошибка.
     */
    void sendStationError(Station station);

}
