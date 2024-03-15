package sber.school.ChargingNetwork.service;

import sber.school.ChargingNetwork.dto.StartSessionResponseDto;
import sber.school.ChargingNetwork.dto.StopSessionResponseDto;
import sber.school.ChargingNetwork.dto.StartSessionRequestDto;
import sber.school.ChargingNetwork.dto.StopSessionRequestDto;

public interface SessionService {

    StartSessionResponseDto startSession(StartSessionRequestDto request, int stationId);
    StopSessionResponseDto stopSession(StopSessionRequestDto request);

}
