package sber.school.ChargingNetwork.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import sber.school.ChargingNetwork.model.station.Station;
import sber.school.ChargingNetwork.repository.StationRepository;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationStationDetailsServiceImplTest {

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private ApplicationStationDetailsServiceImpl stationDetailsService;

    @Test
    public void loadUserByUsername_validUsername_returnUser() {
        var username = "user1";
        var station = new Station(1, "ЭЗС1", "user1", "password1");
        var authority = new SimpleGrantedAuthority("ROLE_STATION");

        doReturn(station).when(stationRepository).findByLogin("user1");

        var userDetails = stationDetailsService.loadUserByUsername(username);

        verify(stationRepository, times(1)).findByLogin("user1");
        assertAll(
                () -> assertEquals("user1", userDetails.getUsername()),
                () -> assertEquals("password1", userDetails.getPassword()),
                () -> assertEquals(1, userDetails.getAuthorities().size()),
                () -> assertTrue(userDetails.getAuthorities().contains(authority))
        );
    }

    @Test
    public void loadUserByUsername_invalidUsername_throwUsernameNotFoundException() {
        var username = "user2";

        doThrow(NoSuchElementException.class).when(stationRepository).findByLogin("user2");

        assertThrows(UsernameNotFoundException.class, () -> stationDetailsService.loadUserByUsername(username));

        verify(stationRepository, times(1)).findByLogin("user2");
    }
}