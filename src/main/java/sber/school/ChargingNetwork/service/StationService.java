package sber.school.ChargingNetwork.service;

import sber.school.ChargingNetwork.model.station.Station;

public interface StationService {

    Station saveStation(Station station);
    Iterable<Station> getAllStations();
    Station getStationById(int id);
    void updateStation(int id, Station station);
    void deleteStation(int id);
    void setStationState(int id, String state);
    Station findByLogin(String login);

}
