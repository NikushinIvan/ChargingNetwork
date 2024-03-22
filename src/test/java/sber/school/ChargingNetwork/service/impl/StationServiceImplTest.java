package sber.school.ChargingNetwork.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import sber.school.ChargingNetwork.model.station.Station;
import sber.school.ChargingNetwork.model.station.Vendor;
import sber.school.ChargingNetwork.repository.StationRepository;
import sber.school.ChargingNetwork.service.TelegramBotService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static sber.school.ChargingNetwork.dto.StationState.*;

@ExtendWith(MockitoExtension.class)
class StationServiceImplTest {

    @Mock
    private TelegramBotService telegramBotService;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StationServiceImpl stationService;

    @Test
    public void saveStation_returnSavedStation() {
        var station = mock(Station.class);

        doReturn("password").when(station).getPassword();
        doReturn(station).when(stationRepository).save(station);

        var result = stationService.saveStation(station);

        verify(passwordEncoder, times(1)).encode("password");
        verify(stationRepository, times(1)).save(station);
        assertEquals(station, result);
    }

    @Test
    public void getAllStations_returnListStations() {
        var station = new Station(1, "ЭЗС1", "CP1", "pass1");
        var station1 = new Station(2, "ЭЗС2", "CP2", "pass2");

        doReturn(List.of(station, station1)).when(stationRepository).findAll();

        var result = stationService.getAllStations();

        verify(stationRepository, times(1)).findAll();
        assertAll(
                () -> assertEquals(2, StreamSupport.stream(result.spliterator(), false).count()),
                () -> assertTrue(StreamSupport.stream(
                        result.spliterator(), false).anyMatch(s -> s.getStationId() == 1)),
                () -> assertTrue(StreamSupport.stream(
                        result.spliterator(), false).anyMatch(s -> s.getStationId() == 2))
        );
    }

    @Test
    public void getStationById_validId_returnStation() {
        var station = new Station(3, "ЭЗС3", "CP3", "pass3");
        var id = 3;

        doReturn(Optional.of(station)).when(stationRepository).findById(3);

        var result = stationService.getStationById(id);

        verify(stationRepository, times(1)).findById(3);
        assertEquals(station, result);
    }

    @Test
    public void getStationById_invalidId_returnStation() {
        var id = 4;

        doReturn(Optional.empty()).when(stationRepository).findById(4);

        assertThrows(NoSuchElementException.class, () -> stationService.getStationById(id));

        verify(stationRepository, times(1)).findById(4);
    }

    @Test
    public void updateStation_returnStation() {
        var updatedStation = mock(Station.class);
        var id = 4;
        var station = new Station();
        station.setVendor(new Vendor());

        doReturn(updatedStation).when(stationRepository).save(updatedStation);
        doReturn(Optional.of(station)).when(stationRepository).findById(4);

        stationService.updateStation(id, updatedStation);

        verify(stationRepository, times(1)).save(updatedStation);
        verify(updatedStation, times(1)).setStationId(4);
        verify(updatedStation, times(1)).setVendor(station.getVendor());
    }

    @Test
    public void deleteStation() {
        var id = 5;

        doNothing().when(stationRepository).deleteById(5);

        stationService.deleteStation(id);

        verify(stationRepository, times(1)).deleteById(5);
    }

    @Test
    public void setStationState_waitState() {
        var id = 6;
        var station = mock(Station.class);

        doReturn(Optional.of(station)).when(stationRepository).findById(6);

        stationService.setStationState(id, WAIT);

        verify(stationRepository, times(1)).findById(6);
        verify(station, times(1)).setStationState(WAIT.name());
        verify(stationRepository, times(1)).save(station);
        verify(telegramBotService, never()).sendStationError(station);
    }

    @Test
    public void setStationState_errorState() {
        var id = 7;
        var station = mock(Station.class);

        doReturn(Optional.of(station)).when(stationRepository).findById(7);

        stationService.setStationState(id, ERROR);

        verify(stationRepository, times(1)).findById(7);
        verify(station, times(1)).setStationState(ERROR.name());
        verify(stationRepository, times(1)).save(station);
        verify(telegramBotService, times(1)).sendStationError(station);
    }

    @Test
    public void setStationState_nullState() {
        var id = 8;

        assertThrows(NullPointerException.class, () -> stationService.setStationState(id, null));
    }

    @Test
    public void setStationState_invalidId() {
        var id = 8;

        doReturn(Optional.empty()).when(stationRepository).findById(8);

        assertThrows(NoSuchElementException.class, () -> stationService.setStationState(id, CHARGE));

        verify(stationRepository, times(1)).findById(8);
        verify(stationRepository, never()).save(any(Station.class));
        verify(telegramBotService, never()).sendStationError(any(Station.class));
    }
}