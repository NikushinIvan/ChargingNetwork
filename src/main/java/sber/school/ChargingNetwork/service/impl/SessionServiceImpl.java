package sber.school.ChargingNetwork.service.impl;

import org.springframework.stereotype.Service;
import sber.school.ChargingNetwork.dto.*;
import sber.school.ChargingNetwork.model.chargeSession.ChargeSession;
import sber.school.ChargingNetwork.repository.ChargeSessionRepository;
import sber.school.ChargingNetwork.service.SessionService;
import sber.school.ChargingNetwork.service.StationService;
import sber.school.ChargingNetwork.service.UserService;

import java.io.InvalidObjectException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static sber.school.ChargingNetwork.dto.ResponseStatus.ACCEPTED;
import static sber.school.ChargingNetwork.dto.ResponseStatus.REJECTED;
import static sber.school.ChargingNetwork.dto.StationState.WAIT;

@Service
public class SessionServiceImpl implements SessionService {

    private final StationService stationService;
    private final UserService userService;
    private final ChargeSessionRepository chargeSessionRepository;

    public SessionServiceImpl(StationService stationService, UserService userService, ChargeSessionRepository chargeSessionRepository) {
        this.stationService = stationService;
        this.userService = userService;
        this.chargeSessionRepository = chargeSessionRepository;
    }

    @Override
    public StartSessionResponseDto startSession(StartSessionRequestDto request, int id) {
        if (request.getUid() == null) {
            return new StartSessionResponseDto(REJECTED);
        }
        try {
            var user = userService.getUserByUid(request.getUid());
            var station = stationService.getStationById(id);
            if (!station.getStationState().equals(WAIT.name())) {
                return new StartSessionResponseDto(REJECTED);
            }
            var session = new ChargeSession(user, station, LocalDateTime.now());
            var savedSession = chargeSessionRepository.save(session);
            return new StartSessionResponseDto(ACCEPTED, savedSession.getSessionId());
        } catch ( NoSuchElementException e) {
            return new StartSessionResponseDto(REJECTED);
        }
    }

    @Override
    public StopSessionResponseDto stopSession(StopSessionRequestDto request) {
        var sessionId = request.getChargeSessionId();
        var session = chargeSessionRepository.findById(sessionId);
        if (session.isPresent()) {
            if (request.getStopReason() != null) {
                session.get().setStopReason(request.getStopReason());
            }
            session.get().setStopTime(LocalDateTime.now());
            chargeSessionRepository.save(session.get());
            return new StopSessionResponseDto(ACCEPTED);
        }
        return new StopSessionResponseDto(REJECTED);
    }
}
