package sber.school.ChargingNetwork.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sber.school.ChargingNetwork.dto.ResponseDto;
import sber.school.ChargingNetwork.dto.StartSessionRequestDto;
import sber.school.ChargingNetwork.dto.StationStateRequestDto;
import sber.school.ChargingNetwork.dto.StopSessionRequestDto;
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
        stationService.setStationState(id, stationState.getStationState());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<ResponseDto> startSession(@RequestBody StartSessionRequestDto request,
                                                       @PathVariable int id) {
        var response = sessionService.startSession(request, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/stop")
    public ResponseEntity<ResponseDto> stopSession(@RequestBody StopSessionRequestDto request) {
        var response = sessionService.stopSession(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
