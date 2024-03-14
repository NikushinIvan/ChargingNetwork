package sber.school.ChargingNetwork.service.impl;

import org.springframework.stereotype.Service;
import sber.school.ChargingNetwork.dto.ResponseDto;
import sber.school.ChargingNetwork.dto.StartSessionRequestDto;
import sber.school.ChargingNetwork.dto.StationStateRequestDto;
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
    public ResponseDto startSession(StartSessionRequestDto request, int id) {
        try {
            var user = userService.getByUid(request.getUid());
            var station = stationService.getStationById(id);
            if (!"WAIT".equals(station.getStationState())) {
                return new ResponseDto("REJECTED");
            }
            var startTime = LocalDateTime.now();
            var session = new ChargeSession(user, station, startTime);
            chargeSessionRepository.save(session);
            return new ResponseDto("ACCEPTED");
        } catch ( NoSuchElementException e) {
            return new ResponseDto("REJECTED");
        }
    }

    @Override
    public ResponseDto stopSession(StopSessionRequestDto request) {
        var sessionId = request.getChargeSessionId();
        var stopReason = request.getStopReason();
        var session = chargeSessionRepository.findById(sessionId);
        if (session.isPresent()) {
            session.get().setStopReason(stopReason);
            session.get().setStopTime(LocalDateTime.now());
            chargeSessionRepository.save(session.get());
            return new ResponseDto("ACCEPTED");
        }
        return new ResponseDto("REJECTED");
    }
}
