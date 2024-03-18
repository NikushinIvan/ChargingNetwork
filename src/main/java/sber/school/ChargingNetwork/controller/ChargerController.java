package sber.school.ChargingNetwork.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sber.school.ChargingNetwork.dto.*;
import sber.school.ChargingNetwork.service.SessionService;
import sber.school.ChargingNetwork.service.StationService;

@RestController
@RequestMapping("charger/api")
public class ChargerController {

    private final StationService stationService;
    private final SessionService sessionService;

    public ChargerController(StationService stationService, SessionService sessionService) {
        this.stationService = stationService;
        this.sessionService = sessionService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> setStationState(@RequestBody StationStateRequestDto stationState,
                                                @PathVariable int id) {
        try {
            stationService.setStationState(id, stationState.getStationState());
        } catch (NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<StartSessionResponseDto> startSession(@RequestBody StartSessionRequestDto request,
                                                                @PathVariable int id) {
        var response = sessionService.startSession(request, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/stop")
    public ResponseEntity<StopSessionResponseDto> stopSession(@RequestBody StopSessionRequestDto request) {
        var response = sessionService.stopSession(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
