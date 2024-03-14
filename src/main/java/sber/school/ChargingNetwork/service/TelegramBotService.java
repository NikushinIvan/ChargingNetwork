package sber.school.ChargingNetwork.service;

import sber.school.ChargingNetwork.model.station.Station;

public interface TelegramBotService {

    void sendStationError(Station station);

}
