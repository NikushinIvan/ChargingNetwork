package sber.school.ChargingNetwork.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sber.school.ChargingNetwork.dto.StartSessionRequestDto;
import sber.school.ChargingNetwork.dto.StopSessionRequestDto;
import sber.school.ChargingNetwork.model.chargeSession.ChargeSession;
import sber.school.ChargingNetwork.model.station.Station;
import sber.school.ChargingNetwork.model.user.User;
import sber.school.ChargingNetwork.repository.ChargeSessionRepository;
import sber.school.ChargingNetwork.service.StationService;
import sber.school.ChargingNetwork.service.UserService;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static sber.school.ChargingNetwork.dto.ResponseStatus.ACCEPTED;
import static sber.school.ChargingNetwork.dto.ResponseStatus.REJECTED;
import static sber.school.ChargingNetwork.dto.StationState.ERROR;
import static sber.school.ChargingNetwork.dto.StationState.WAIT;

@ExtendWith(MockitoExtension.class)
class SessionServiceImplTest {

    @Mock
    private StationService stationService;
    @Mock
    private UserService userService;
    @Mock
    private ChargeSessionRepository chargeSessionRepository;

    @InjectMocks
    private SessionServiceImpl sessionService;

    @Test
    public void startSession_validRequest_returnAcceptedStatus() {
        var request = new StartSessionRequestDto("11223344");
        var id = 1;
        var user = new User("11223344");
        var station = new Station(WAIT.name());

        doReturn(user).when(userService).getUserByUid("11223344");
        doReturn(station).when(stationService).getStationById(1);
        doAnswer(
                invocation -> {
                    ChargeSession chargeSession = invocation.getArgument(0);
                    chargeSession.setSessionId(1);
                    return chargeSession;
                }
                ).when(chargeSessionRepository).save(any(ChargeSession.class));

        var response = sessionService.startSession(request, id);

        verify(userService, times(1)).getUserByUid("11223344");
        verify(stationService, times(1)).getStationById(1);
        verify(chargeSessionRepository, times(1)).save(any(ChargeSession.class));
        assertAll(
                () -> assertEquals(ACCEPTED, response.getStatus()),
                () -> assertEquals(1, response.getSessionId()));
    }

    @Test
    public void startSession_nullRequest_returnRejectedStatus() {
        var request = new StartSessionRequestDto();
        var id = 2;

        var response = sessionService.startSession(request, id);

        verify(userService, never()).getUserByUid(anyString());
        verify(stationService, never()).getStationById(anyInt());
        assertAll(
                () -> assertEquals(REJECTED, response.getStatus()),
                () -> assertEquals(0, response.getSessionId()));
    }

    @Test
    public void startSession_stationNotReady_returnRejectedStatus() {
        var request = new StartSessionRequestDto("00001111");
        var id = 3;
        var user = new User("00001111");
        var station = new Station(ERROR.name());

        doReturn(user).when(userService).getUserByUid("00001111");
        doReturn(station).when(stationService).getStationById(3);

        var response = sessionService.startSession(request, id);

        verify(userService, times(1)).getUserByUid("00001111");
        verify(stationService, times(1)).getStationById(3);
        assertAll(
                () -> assertEquals(REJECTED, response.getStatus()),
                () -> assertEquals(0, response.getSessionId()));
    }

    @Test
    public void startSession_userNotFound_returnRejectedStatus() {
        var request = new StartSessionRequestDto("0000aaaa");
        var id = 4;

        doThrow(NoSuchElementException.class).when(userService).getUserByUid("0000aaaa");

        var response = sessionService.startSession(request, id);

        verify(userService, times(1)).getUserByUid("0000aaaa");
        verify(stationService, never()).getStationById(anyInt());
        assertAll(
                () -> assertEquals(REJECTED, response.getStatus()),
                () -> assertEquals(0, response.getSessionId()));
    }

    @Test
    public void startSession_stationNotFound_returnRejectedStatus() {
        var request = new StartSessionRequestDto("ccccaaaa");
        var id = 5;
        var user = new User("ccccaaaa");

        doReturn(user).when(userService).getUserByUid("ccccaaaa");
        doThrow(NoSuchElementException.class).when(stationService).getStationById(5);

        var response = sessionService.startSession(request, id);

        verify(userService, times(1)).getUserByUid("ccccaaaa");
        verify(stationService, times(1)).getStationById(5);
        assertAll(
                () -> assertEquals(REJECTED, response.getStatus()),
                () -> assertEquals(0, response.getSessionId()));
    }

    @Test
    public void stopSession_validRequest_returnAcceptedStatus() {
        var request = new StopSessionRequestDto(1, "EVStop");
        var chargeSession = mock(ChargeSession.class);

        doReturn(Optional.of(chargeSession)).when(chargeSessionRepository).findById(1);

        var response = sessionService.stopSession(request);

        verify(chargeSessionRepository, times(1)).findById(1);
        verify(chargeSession, times(1)).setStopReason("EVStop");
        verify(chargeSession, times(1)).setStopTime(any(LocalDateTime.class));
        verify(chargeSessionRepository, times(1)).save(chargeSession);
        assertEquals(ACCEPTED, response.getStatus());
    }

    @Test
    public void stopSession_validRequestAnotherStopReason_returnAcceptedStatus() {
        var request = new StopSessionRequestDto();
        request.setChargeSessionId(2);
        var chargeSession = mock(ChargeSession.class);

        doReturn(Optional.of(chargeSession)).when(chargeSessionRepository).findById(2);

        var response = sessionService.stopSession(request);

        verify(chargeSessionRepository, times(1)).findById(2);
        verify(chargeSession, never()).setStopReason(anyString());
        verify(chargeSession, times(1)).setStopTime(any(LocalDateTime.class));
        verify(chargeSessionRepository, times(1)).save(chargeSession);
        assertEquals(ACCEPTED, response.getStatus());
    }

    @Test
    public void stopSession_invalidSessionId_returnRejectedStatus() {
        var request = new StopSessionRequestDto();
        request.setChargeSessionId(3);

        doReturn(Optional.empty()).when(chargeSessionRepository).findById(3);

        var response = sessionService.stopSession(request);

        verify(chargeSessionRepository, times(1)).findById(3);
        assertEquals(REJECTED, response.getStatus());
    }
}