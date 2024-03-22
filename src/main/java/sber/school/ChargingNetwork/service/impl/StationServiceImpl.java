package sber.school.ChargingNetwork.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sber.school.ChargingNetwork.dto.StationState;
import sber.school.ChargingNetwork.model.station.Station;
import sber.school.ChargingNetwork.repository.StationRepository;
import sber.school.ChargingNetwork.service.StationService;
import sber.school.ChargingNetwork.service.TelegramBotService;

import java.util.NoSuchElementException;

import static sber.school.ChargingNetwork.dto.StationState.DISCONNECT;
import static sber.school.ChargingNetwork.dto.StationState.ERROR;

@Service
public class StationServiceImpl implements StationService {

    private final TelegramBotService telegramBotService;
    private final StationRepository stationRepository;

    private final PasswordEncoder passwordEncoder;


    public StationServiceImpl(TelegramBotService telegramBotService, StationRepository stationRepository, PasswordEncoder passwordEncoder) {
        this.telegramBotService = telegramBotService;
        this.stationRepository = stationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Station saveStation(Station station) {
        station.setPassword(passwordEncoder.encode(station.getPassword()));
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
        var station = stationRepository.findById(id).get();
        updatedStation.setLogin(station.getLogin());
        updatedStation.setPassword(station.getPassword());
        updatedStation.setVendor(station.getVendor());
        stationRepository.save(updatedStation);
    }

    @Override
    public void deleteStation(int id) {
        stationRepository.deleteById(id);
    }

    @Override
    public void setStationState(int id, StationState state) {
        if (state == null) {
            throw new NullPointerException("Поле state запроса не должно быть пустым");
        }
        var station = stationRepository.findById(id);
        station.ifPresentOrElse(
                s -> {
                    s.setStationState(state.name());
                    stationRepository.save(s);
                    if (state == ERROR || state == DISCONNECT) {
                        telegramBotService.sendStationError(s);
                    }
                },
                () -> {
                    throw new NoSuchElementException("Станция не найдена");
                });
    }
}
