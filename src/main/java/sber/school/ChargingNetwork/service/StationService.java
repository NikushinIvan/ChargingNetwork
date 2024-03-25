package sber.school.ChargingNetwork.service;

import org.springframework.http.ResponseEntity;
import sber.school.ChargingNetwork.dto.StationState;
import sber.school.ChargingNetwork.model.station.Station;

public interface StationService {

    Station saveStation(Station station);
    Iterable<Station> getAllStations();
    Station getStationById(int id);
    void updateStation(int id, Station station);
    void deleteStation(int id);
    ResponseEntity<Void> setStationState(int id, StationState state);
}
