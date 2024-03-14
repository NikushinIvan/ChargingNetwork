package sber.school.ChargingNetwork.service;

import sber.school.ChargingNetwork.dto.ResponseDto;
import sber.school.ChargingNetwork.dto.StartSessionRequestDto;
import sber.school.ChargingNetwork.dto.StopSessionRequestDto;

public interface SessionService {

    ResponseDto startSession(StartSessionRequestDto request, int stationId);
    ResponseDto stopSession(StopSessionRequestDto request);

}
