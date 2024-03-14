package sber.school.ChargingNetwork.service.impl;

import org.springframework.stereotype.Service;
import sber.school.ChargingNetwork.model.station.Station;
import sber.school.ChargingNetwork.repository.StationRepository;
import sber.school.ChargingNetwork.service.StationService;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;

    public StationServiceImpl(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public Station saveStation(Station station) {
        return stationRepository.save(station);
    }

    @Override
    public Iterable<Station> getAllStations() {
        return stationRepository.findAll();
    }

    @Override
    public Station getStationById(int id) {
        var station = stationRepository.findById(id);
        if (station.isPresent()) {
            return station.get();
        } else {
            throw new NoSuchElementException("Станция не найдена");
        }
    }

    @Override
    public void updateStation(int id, Station updatedStation) {
        updatedStation.setStationId(id);
        updatedStation.setVendor(stationRepository.findById(id).get().getVendor());
        stationRepository.save(updatedStation);
    }

    @Override
    public void deleteStation(int id) {
        stationRepository.deleteById(id);
    }

    @Override
    public void setStationState(int id, String state) {
        var station = stationRepository.findById(id);
        station.ifPresentOrElse(
                s -> {
                    s.setStationState(state);
                    stationRepository.save(s);
                },
                () -> {
                    throw new NoSuchElementException("Станция не найдена");
                });
    }
}
