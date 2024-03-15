package sber.school.ChargingNetwork.service.impl;

import org.springframework.stereotype.Service;
import sber.school.ChargingNetwork.dto.StartSessionResponseDto;
import sber.school.ChargingNetwork.dto.StopSessionResponseDto;
import sber.school.ChargingNetwork.dto.StartSessionRequestDto;
import sber.school.ChargingNetwork.dto.StopSessionRequestDto;
import sber.school.ChargingNetwork.model.chargeSession.ChargeSession;
import sber.school.ChargingNetwork.repository.ChargeSessionRepository;
import sber.school.ChargingNetwork.service.SessionService;
import sber.school.ChargingNetwork.service.StationService;
import sber.school.ChargingNetwork.service.UserService;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

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
        try {
            var user = userService.getUserByUid(request.getUid());
            var station = stationService.getStationById(id);
            if (!"WAIT".equals(station.getStationState())) {
                return new StartSessionResponseDto("REJECTED");
            }
            var startTime = LocalDateTime.now();
            var session = new ChargeSession(user, station, startTime);
            var savedSession = chargeSessionRepository.save(session);
            return new StartSessionResponseDto("ACCEPTED", savedSession.getSessionId());
        } catch ( NoSuchElementException e) {
            return new StartSessionResponseDto("REJECTED");
        }
    }

    @Override
    public StopSessionResponseDto stopSession(StopSessionRequestDto request) {
        var sessionId = request.getChargeSessionId();
        var stopReason = request.getStopReason();
        var session = chargeSessionRepository.findById(sessionId);
        if (session.isPresent()) {
            session.get().setStopReason(stopReason);
            session.get().setStopTime(LocalDateTime.now());
            chargeSessionRepository.save(session.get());
            return new StopSessionResponseDto("ACCEPTED");
        }
        return new StopSessionResponseDto("REJECTED");
    }
}
