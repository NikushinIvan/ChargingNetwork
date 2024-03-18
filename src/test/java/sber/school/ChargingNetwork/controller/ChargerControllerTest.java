package sber.school.ChargingNetwork.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import sber.school.ChargingNetwork.dto.*;
import sber.school.ChargingNetwork.service.SessionService;
import sber.school.ChargingNetwork.service.StationService;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static sber.school.ChargingNetwork.dto.ResponseStatus.ACCEPTED;
import static sber.school.ChargingNetwork.dto.ResponseStatus.REJECTED;
import static sber.school.ChargingNetwork.dto.StationState.WAIT;

@ExtendWith(MockitoExtension.class)
class ChargerControllerTest {

    @Mock
    private StationService stationService;
    @Mock
    private SessionService sessionService;

    @InjectMocks
    private ChargerController chargerController;

    @Test
    public void setStationState_validRequest_statusOk() {
        var request = new StationStateRequestDto(WAIT);
        var id = 1;

        doNothing().when(stationService).setStationState(1, WAIT);

        var response = chargerController.setStationState(request, id);

        verify(stationService, times(1)).setStationState(1, WAIT);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void setStationState_nullStationState_httpCode400() {
        var request = new StationStateRequestDto();
        var id = 1;

        doThrow(NullPointerException.class).when(stationService).setStationState(1, null);

        var response = chargerController.setStationState(request, id);

        verify(stationService, times(1)).setStationState(1, null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void startSession_validRequest_acceptedStatus() {
        var request = new StartSessionRequestDto("00112233");
        var id = 1;

        doReturn(new StartSessionResponseDto(ACCEPTED, 1))
                .when(sessionService).startSession(request, 1);

        var response = chargerController.startSession(request, id);

        verify(sessionService, times(1)).startSession(request, 1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ACCEPTED, response.getBody().getStatus());
        assertEquals(1, response.getBody().getSessionId());
    }

    @Test
    public void startSession_invalidUid_rejectedStatus() {
        var request = new StartSessionRequestDto("00vxxzaa");
        var id = 2;

        doReturn(new StartSessionResponseDto(REJECTED))
                .when(sessionService).startSession(request, 2);

        var response = chargerController.startSession(request, id);

        verify(sessionService, times(1)).startSession(request, 2);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(REJECTED, response.getBody().getStatus());
        assertEquals(0, response.getBody().getSessionId());
    }

    @Test
    public void startSession_nullUid_rejectedStatus() {
        var request = new StartSessionRequestDto();
        var id = 3;

        doReturn(new StartSessionResponseDto(REJECTED))
                .when(sessionService).startSession(request, 3);

        var response = chargerController.startSession(request, id);

        verify(sessionService, times(1)).startSession(request, 3);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(REJECTED, response.getBody().getStatus());
        assertEquals(0, response.getBody().getSessionId());
    }

    @Test
    public void stopSession_validRequest_acceptedStatus() {
        var request = new StopSessionRequestDto(5, "EvStop");

        doReturn(new StopSessionResponseDto(ACCEPTED)).when(sessionService).stopSession(request);

        var response = chargerController.stopSession(request);

        verify(sessionService, times(1)).stopSession(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ACCEPTED, response.getBody().getStatus());
    }

    @Test
    public void stopSession_validRequestWithoutStopReason_acceptedStatus() {
        var request = new StopSessionRequestDto(5, null);

        doReturn(new StopSessionResponseDto(ACCEPTED)).when(sessionService).stopSession(request);

        var response = chargerController.stopSession(request);

        verify(sessionService, times(1)).stopSession(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ACCEPTED, response.getBody().getStatus());
    }

    @Test
    public void stopSession_invalidChargeSessionId_rejectedStatus() {
        var request = new StopSessionRequestDto(6, null);

        doReturn(new StopSessionResponseDto(REJECTED)).when(sessionService).stopSession(request);

        var response = chargerController.stopSession(request);

        verify(sessionService, times(1)).stopSession(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(REJECTED, response.getBody().getStatus());
    }
}