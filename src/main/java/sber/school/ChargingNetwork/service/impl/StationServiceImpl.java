package sber.school.ChargingNetwork.service.impl;

import org.springframework.stereotype.Service;
import sber.school.ChargingNetwork.model.station.Station;
import sber.school.ChargingNetwork.repository.StationRepository;
import sber.school.ChargingNetwork.service.StationService;
import sber.school.ChargingNetwork.service.TelegramBotService;

import java.util.NoSuchElementException;

@Service
public class StationServiceImpl implements StationService {

    private final TelegramBotService telegramBotService;
    private final StationRepository stationRepository;

    public StationServiceImpl(TelegramBotService telegramBotService, StationRepository stationRepository) {
        this.telegramBotService = telegramBotService;
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
                    if ("ERROR".equals(state) || "DISCONNECT".equals(state)) {
                        telegramBotService.sendStationError(s);
                    }
                },
                () -> {
                    throw new NoSuchElementException("Станция не найдена");
                });
    }

    @Override
    public Station findByLogin(String login) {
        var station = stationRepository.findByLogin(login);
        if (station != null) {
            return station;
        } else {
            throw new NoSuchElementException("Станция не найдена");
        }
    }
}
