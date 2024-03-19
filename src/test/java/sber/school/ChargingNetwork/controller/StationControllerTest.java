package sber.school.ChargingNetwork.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import sber.school.ChargingNetwork.model.station.Address;
import sber.school.ChargingNetwork.model.station.Station;
import sber.school.ChargingNetwork.model.station.Vendor;
import sber.school.ChargingNetwork.model.user.User;
import sber.school.ChargingNetwork.service.AddressService;
import sber.school.ChargingNetwork.service.StationService;
import sber.school.ChargingNetwork.service.UserService;
import sber.school.ChargingNetwork.service.VendorService;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class StationControllerTest {

    @Mock
    private StationService stationService;
    @Mock
    private UserService userService;
    @Mock
    private AddressService addressService;
    @Mock
    private VendorService vendorService;

    @InjectMocks
    private StationController stationController;

    @Test
    public void getAllStations_validRequest_returnViewNameAndListStations() {
        var model = mock(Model.class);
        var stations = List.of(
                new Station(1, "ЭЗС1", new Vendor(), new Address()),
                new Station(2, "ЭЗС2", new Vendor(), new Address()),
                new Station(3, "ЭЗС3", new Vendor(), new Address()));

        doReturn(stations).when(stationService).getAllStations();

        var response = stationController.getAllStations(model);

        verify(stationService, times(1)).getAllStations();
        verify(model, times(1)).addAttribute("stations", stations);
        assertEquals("station/station", response);
    }

    @Test
    public void getStation_validRequest_returnViewNameAndStation() {
        var model = mock(Model.class);
        var id = 1;
        var station = new Station(1, "ЭЗС1", new Vendor(), new Address());

        doReturn(station).when(stationService).getStationById(1);

        var response = stationController.getStation(model, id);

        verify(stationService, times(1)).getStationById(1);
        verify(model, times(1)).addAttribute("station", station);
        assertEquals("/station/profile", response);
    }

    @Test
    public void getStation_invalidStationId_throwNoSuchElementException() {
        var model = mock(Model.class);
        var id = 2;

        doThrow(NoSuchElementException.class).when(stationService).getStationById(2);

        assertThrows(NoSuchElementException.class, () -> stationController.getStation(model, id));

        verify(stationService, times(1)).getStationById(2);
        verify(model, never()).addAttribute(anyString(), any());
    }

    @Test
    public void createStation_validRequest_returnRedirect() {
        var station = new Station(1, "ЭЗС1", new Vendor(), new Address());

        doReturn(station).when(stationService).saveStation(station);

        var response = stationController.createStation(station);

        verify(stationService, times(1)).saveStation(station);
        assertEquals("redirect:/station", response);
    }

    @Test
    public void deleteStation_validRequest_returnRedirect() {
        var id = 3;

        doNothing().when(stationService).deleteStation(3);

        var response = stationController.deleteStation(id);

        verify(stationService, times(1)).deleteStation(3);
        assertEquals("redirect:/station", response);
    }

    @Test
    public void updateStation_validRequest_returnRedirect() {
        var station = new Station(0, "ЭЗС1", new Vendor(), new Address());
        var id = 4;

        doNothing().when(stationService).updateStation(4, station);

        var response = stationController.updateStation(id, station);

        verify(stationService, times(1)).updateStation(4, station);
        assertEquals("redirect:/station", response);
    }

    @Test
    public void showManagerPanel_validRequest_returnViewName() {

        assertEquals("/station/managerPanel", stationController.showManagerPanel());

    }

    @Test
    public void showCreatePage_validRequest_returnViewName() {
        var model = mock(Model.class);
        var addresses = List.of(new Address());
        var vendors = List.of(new Vendor());
        var users = List.of(new User());

        doReturn(addresses).when(addressService).getAllAddress();
        doReturn(vendors).when(vendorService).getAllVendors();
        doReturn(users).when(userService).getUsersGetWithRole("ROLE_MANAGER_STATION");

        var response = stationController.showCreatePage(model);

        verify(addressService, times(1)).getAllAddress();
        verify(vendorService, times(1)).getAllVendors();
        verify(userService, times(1)).getUsersGetWithRole("ROLE_MANAGER_STATION");
        verify(model, times(1)).addAttribute(eq("station"), any(Station.class));
        verify(model, times(1)).addAttribute("addresses", addresses);
        verify(model, times(1)).addAttribute("vendors", vendors);
        verify(model, times(1)).addAttribute("users", users);
        assertEquals("/station/create", response);
    }

    @Test
    public void showUpdatePage_validRequest_returnViewName() {
        var model = mock(Model.class);
        var id = 5;
        var station = new Station();
        var addresses = List.of(new Address());
        var users = List.of(new User());

        doReturn(station).when(stationService).getStationById(5);
        doReturn(addresses).when(addressService).getAllAddress();
        doReturn(users).when(userService).getUsersGetWithRole("ROLE_MANAGER_STATION");

        var response = stationController.showUpdatePage(model, id);

        verify(stationService, times(1)).getStationById(5);
        verify(addressService, times(1)).getAllAddress();
        verify(userService, times(1)).getUsersGetWithRole("ROLE_MANAGER_STATION");
        verify(model, times(1)).addAttribute("station", station);
        verify(model, times(1)).addAttribute("addresses", addresses);
        verify(model, times(1)).addAttribute("users", users);
        assertEquals("/station/update", response);
    }
}