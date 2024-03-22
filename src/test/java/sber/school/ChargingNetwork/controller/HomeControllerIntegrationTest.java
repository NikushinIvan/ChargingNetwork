package sber.school.ChargingNetwork.controller;

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
import sber.school.ChargingNetwork.model.user.Role;
import sber.school.ChargingNetwork.model.user.User;
import sber.school.ChargingNetwork.service.ApplicationStationDetailsService;
import sber.school.ChargingNetwork.service.ApplicationUserDetailsService;
import sber.school.ChargingNetwork.service.UserService;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebMvcTest(HomeController.class)
@Import(SecurityConfig.class)
class HomeControllerIntegrationTest {

    @MockBean
    private UserService userService;
    @MockBean
    private ApplicationUserDetailsService userDetailsService;
    @MockBean
    private ApplicationStationDetailsService stationDetailsService;

    @Autowired
    private MockMvc mockMvc;

    private static User user1;
    private static User user2;
    private static User user3;

    @BeforeAll
    static void beforeAll() {
        user1 = new User(1, "user1", "Daria", "Ivanova", null);
        user1.setRoles(Set.of(new Role(1, "ROLE_ADMIN")));
        user2 = new User(2, "user2", "Ivan", "Petrov", null);
        user2.setRoles(Set.of(new Role(2, "ROLE_MANAGER_USER")));
        user3 = new User("user3", "Иван", "Никитин", null);
        user3.setRoles(Set.of(new Role(3, "ROLE_MANAGER_STATION")));
    }

    @Test
    @WithMockUser(username = "user1", roles = "ADMIN")
    public void getHomePage_admin_returnOk() throws Exception {
        when(userService.getUserByUsername("user1")).thenReturn(user1);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/home"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", user1))
                .andExpect(MockMvcResultMatchers.model().attribute("roles", List.of("ROLE_ADMIN")));
    }

    @Test
    @WithMockUser(username = "user2", roles = "MANAGER_USER")
    public void getHomePage_managerUser_returnOk() throws Exception {
        when(userService.getUserByUsername("user2")).thenReturn(user2);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/home"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", user2))
                .andExpect(MockMvcResultMatchers.model().attribute("roles", List.of("ROLE_MANAGER_USER")));
    }

    @Test
    @WithMockUser(username = "user3", roles = "MANAGER_STATION")
    public void getHomePage_managerStation_return403() throws Exception {
        when(userService.getUserByUsername("user3")).thenReturn(user3);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/home"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", user3))
                .andExpect(MockMvcResultMatchers.model().attribute("roles", List.of("ROLE_MANAGER_STATION")));
    }

    @Test
    @WithAnonymousUser
    public void getHomePage_anonymous_return302() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }
}