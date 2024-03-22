package sber.school.ChargingNetwork.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sber.school.ChargingNetwork.config.SecurityConfig;
import sber.school.ChargingNetwork.model.station.Address;
import sber.school.ChargingNetwork.model.station.Station;
import sber.school.ChargingNetwork.model.station.Vendor;
import sber.school.ChargingNetwork.model.user.User;
import sber.school.ChargingNetwork.service.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(StationController.class)
@Import(SecurityConfig.class)
class StationControllerIntegrationTest {
    @MockBean
    private StationService stationService;
    @MockBean
    private UserService userService;
    @MockBean
    private AddressService addressService;
    @MockBean
    private VendorService vendorService;
    @MockBean
    private ApplicationUserDetailsService userDetailsService;
    @MockBean
    private ApplicationStationDetailsService stationDetailsService;

    @Autowired
    private MockMvc mockMvc;

    private static User user1;
    private static User user2;
    private static User user3;
    private static Station station1;
    private static Station station2;
    private static Vendor vendor1;
    private static Vendor vendor2;
    private static Address address1;
    private static Address address2;

    @BeforeAll
    static void beforeAll() {
        user1 = new User(1, "user1", "Daria", "Ivanova", null);
        user2 = new User(2, "user2", "Ivan", "Petrov", null);
        user3 = new User("user3", "Иван", "Никитин", null);
        vendor1 = new Vendor("компания1", "описание1");
        vendor2 = new Vendor("компания2", "описание2");
        address1 = new Address(1, "Рязань", "Лермонтова", 1);
        address2 = new Address(2, "Рязань", "Ломоносова", 2);
        station1 = new Station(1, "эзс1", vendor1, address1);
        station2 = new Station(2, "эзс2", vendor2, address2);
    }

    @Test
    @WithMockUser(username = "user1", roles = "ADMIN")
    public void getAllStations_admin_returnOk() throws Exception {
        when(stationService.getAllStations()).thenReturn(List.of(station1, station2));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/station"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("station/station"))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("stations", Matchers.contains(station1, station2)));
    }

    @Test
    @WithMockUser(username = "user2", roles = "MANAGER_USER")
    public void getAllStations_managerUser_returnOk() throws Exception {
        when(stationService.getAllStations()).thenReturn(List.of(station1, station2));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/station"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("station/station"))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("stations", Matchers.contains(station1, station2)));
    }

    @Test
    @WithMockUser(username = "user3", roles = "MANAGER_STATION")
    public void getAllStations_managerStation_returnOk() throws Exception {
        when(stationService.getAllStations()).thenReturn(List.of(station1, station2));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/station"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("station/station"))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("stations", Matchers.contains(station1, station2)));
    }

    @Test
    @WithAnonymousUser
    public void getAllStations_anonymous_return302() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.get("/station"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user1", roles = "ADMIN")
    public void getStation_admin_returnOk() throws Exception {
        when(stationService.getStationById(1)).thenReturn(station1);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/station/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/station/profile"))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("station", Matchers.equalToObject(station1)));
    }

    @Test
    @WithMockUser(username = "user2", roles = "MANAGER_USER")
    public void getStation_managerUser_returnOk() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.get("/station/1"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user3", roles = "MANAGER_STATION")
    public void getStation_managerStation_returnOk() throws Exception {
        when(stationService.getStationById(2)).thenReturn(station2);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/station/2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/station/profile"))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("station", Matchers.equalToObject(station2)));
    }

    @Test
    @WithAnonymousUser
    public void getStation_anonymous_return302() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.get("/station/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user1", roles = "ADMIN")
    public void createStation_admin_returnOk() throws Exception {
        var station = mock(Station.class);

        mockMvc
                .perform(MockMvcRequestBuilders.post("/station").flashAttr("station", station))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/station"));
        verify(stationService, times(1)).saveStation(station);
    }

    @Test
    @WithMockUser(username = "user2", roles = "MANAGER_USER")
    public void createStation_managerUser_returnOk() throws Exception {
        var station = mock(Station.class);

        mockMvc
                .perform(MockMvcRequestBuilders.post("/station").flashAttr("station", station))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user3", roles = "MANAGER_STATION")
    public void createStation_managerStation_returnOk() throws Exception {
        var station = mock(Station.class);

        mockMvc
                .perform(MockMvcRequestBuilders.post("/station").flashAttr("station", station))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/station"));
        verify(stationService, times(1)).saveStation(station);
    }

    @Test
    @WithAnonymousUser
    public void createStation_anonymous_return302() throws Exception {
        var station = mock(Station.class);

        mockMvc
                .perform(MockMvcRequestBuilders.post("/station").flashAttr("station", station))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user1", roles = "ADMIN")
    public void deleteStation_admin_returnRedirect() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/station/2"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/station"));
    }

    @Test
    @WithMockUser(username = "user2", roles = "MANAGER_USER")
    public void deleteStation_managerUser_return403() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/station/2"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user3", roles = "MANAGER_STATION")
    public void deleteStation_managerStation_return403() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.delete("/station/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/station"));
    }

    @Test
    @WithAnonymousUser
    public void deleteStation_anonymous_return302() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.delete("/station/2"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user1", roles = "ADMIN")
    public void updateStation_admin_returnRedirect() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.put("/station/2").flashAttr("station", station2))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        verify(stationService, times(1)).updateStation(2, station2);
    }

    @Test
    @WithMockUser(username = "user2", roles = "MANAGER_USER")
    public void updateStation_managerUser_return403() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.put("/station/2").flashAttr("station", station2))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user3", roles = "MANAGER_STATION")
    public void updateStation_managerStation_returnRedirect() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.put("/station/2").flashAttr("station", station2))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        verify(stationService, times(1)).updateStation(2, station2);
    }

    @Test
    @WithAnonymousUser
    public void updateStation_anonymous_return302() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.put("/station/2").flashAttr("station", station2))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user1", roles = "ADMIN")
    public void showManagerPanel_admin_returnViewName() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/station/managerPanel"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/station/managerPanel"));
    }

    @Test
    @WithMockUser(username = "user2", roles = "MANAGER_USER")
    public void showManagerPanel_managerUser_returnViewName() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/station/managerPanel"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user3", roles = "MANAGER_STATION")
    public void showManagerPanel_managerStation_returnViewName() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/station/managerPanel"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/station/managerPanel"));
    }

    @Test
    @WithAnonymousUser
    public void showManagerPanel_anonymous_return302() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.put("/station/managerPanel"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user1", roles = "ADMIN")
    public void showCreatePage_admin_returnViewName() throws Exception {
        when(addressService.getAllAddress()).thenReturn(List.of(address1, address2));
        when(vendorService.getAllVendors()).thenReturn(List.of(vendor1, vendor2));
        when(userService.getUsersGetWithRole("ROLE_MANAGER_STATION")).thenReturn(List.of(user3));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/station/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/station/create"))
                .andExpect(MockMvcResultMatchers.model().attribute("station", Matchers.any(Station.class)))
                .andExpect(MockMvcResultMatchers.model().attribute("addresses", List.of(address1, address2)))
                .andExpect(MockMvcResultMatchers.model().attribute("vendors", List.of(vendor1, vendor2)))
                .andExpect(MockMvcResultMatchers.model().attribute("users", List.of(user3)));
    }

    @Test
    @WithMockUser(username = "user2", roles = "MANAGER_USER")
    public void showCreatePage_managerUser_returnViewName() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/station/create"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user3", roles = "MANAGER_STATION")
    public void showCreatePage_managerStation_returnViewName() throws Exception {
        when(addressService.getAllAddress()).thenReturn(List.of(address1, address2));
        when(vendorService.getAllVendors()).thenReturn(List.of(vendor1, vendor2));
        when(userService.getUsersGetWithRole("ROLE_MANAGER_STATION")).thenReturn(List.of(user3));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/station/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/station/create"))
                .andExpect(MockMvcResultMatchers.model().attribute("station", Matchers.any(Station.class)))
                .andExpect(MockMvcResultMatchers.model().attribute("addresses", List.of(address1, address2)))
                .andExpect(MockMvcResultMatchers.model().attribute("vendors", List.of(vendor1, vendor2)))
                .andExpect(MockMvcResultMatchers.model().attribute("users", List.of(user3)));
    }

    @Test
    @WithAnonymousUser
    public void showCreatePage_anonymous_return302() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.put("/station/create"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user1", roles = "ADMIN")
    public void showUpdatePage_admin_returnViewName() throws Exception {
        when(stationService.getStationById(1)).thenReturn(station1);
        when(addressService.getAllAddress()).thenReturn(List.of(address1, address2));
        when(userService.getUsersGetWithRole("ROLE_MANAGER_STATION")).thenReturn(List.of(user3));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/station/1/update"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/station/update"))
                .andExpect(MockMvcResultMatchers.model().attribute("station", Matchers.equalToObject(station1)))
                .andExpect(MockMvcResultMatchers.model().attribute("addresses", List.of(address1, address2)))
                .andExpect(MockMvcResultMatchers.model().attribute("users", List.of(user3)));
    }

    @Test
    @WithMockUser(username = "user2", roles = "MANAGER_USER")
    public void showUpdatePage_managerUser_returnViewName() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/station/2/update"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user3", roles = "MANAGER_STATION")
    public void showUpdatePage_managerStation_returnViewName() throws Exception {
        when(stationService.getStationById(2)).thenReturn(station2);
        when(addressService.getAllAddress()).thenReturn(List.of(address1, address2));
        when(userService.getUsersGetWithRole("ROLE_MANAGER_STATION")).thenReturn(List.of(user3));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/station/2/update"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/station/update"))
                .andExpect(MockMvcResultMatchers.model().attribute("station", Matchers.equalToObject(station2)))
                .andExpect(MockMvcResultMatchers.model().attribute("addresses", List.of(address1, address2)))
                .andExpect(MockMvcResultMatchers.model().attribute("users", List.of(user3)));
    }

    @Test
    @WithAnonymousUser
    public void showUpdatePage_anonymous_return302() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.put("/station/1/update"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }
}