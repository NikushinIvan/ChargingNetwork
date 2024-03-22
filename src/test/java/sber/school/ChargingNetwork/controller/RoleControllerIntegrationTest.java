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
import sber.school.ChargingNetwork.model.user.Role;
import sber.school.ChargingNetwork.model.user.User;
import sber.school.ChargingNetwork.service.ApplicationStationDetailsService;
import sber.school.ChargingNetwork.service.ApplicationUserDetailsService;
import sber.school.ChargingNetwork.service.RoleService;
import sber.school.ChargingNetwork.service.UserService;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(RoleController.class)
@Import(SecurityConfig.class)
class RoleControllerIntegrationTest {

    @MockBean
    private RoleService roleService;
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
    private static Role admin;
    private static Role managerUser;
    private static Role managerStation;

    @BeforeAll
    static void beforeAll() {
        admin = new Role(1, "ROLE_ADMIN");
        managerUser = new Role(2, "ROLE_MANAGER_USER");
        managerStation = new Role(3, "ROLE_MANAGER_STATION");
        user1 = new User(1, "user1", "Daria", "Ivanova", null);
        user1.setRoles(new HashSet<>(Set.of(admin)));
        user2 = new User(2, "user2", "Ivan", "Petrov", null);
        user2.setRoles(new HashSet<>(Set.of(managerUser)));
        user3 = new User("user3", "Иван", "Никитин", null);
        user3.setRoles(new HashSet<>(Set.of(managerStation)));
    }

    @Test
    @WithMockUser(username = "user1", roles = "ADMIN")
    public void showUpdateUserRolePage_admin_returnViewName() throws Exception {
        var roles = List.of(admin, managerUser, managerStation);

        when(userService.getUserById(2)).thenReturn(user2);
        when(roleService.excludeRepeatRoles(user2.getRoles()))
                .thenReturn(roles);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/role/2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/role/updateUserRole"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", Matchers.equalToObject(user2)))
                .andExpect(MockMvcResultMatchers.model().attribute("roles", Matchers.equalToObject(roles)));
    }

    @Test
    @WithMockUser(username = "user2", roles = "MANAGER_USER")
    public void showUpdateUserRolePage_managerUser_return403() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.get("/role/2"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user3", roles = "MANAGER_STATION")
    public void showUpdateUserRolePage_managerStation_return403() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.get("/role/3"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void showUpdateUserRolePage_anonymous_return302() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.get("/role/3"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user1", roles = "ADMIN")
    public void deleteRoleFromUser_admin_returnRedirect() throws Exception {

        when(userService.getUserById(2)).thenReturn(user2);
        when(roleService.getRoleById(2)).thenReturn(managerUser);

        mockMvc
                .perform(MockMvcRequestBuilders.delete("/role/2/2"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/role/2"));

        verify(userService, times(1)).getUserById(2);
        verify(roleService, times(1)).getRoleById(2);
        verify(userService, times(1)).updateUser(2, user2);
        assertEquals(0, user2.getRoles().size());
    }

    @Test
    @WithMockUser(username = "user2", roles = "MANAGER_USER")
    public void deleteRoleFromUser_managerUser_return403() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.get("/role/2/2"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user3", roles = "MANAGER_STATION")
    public void deleteRoleFromUser_managerStation_return403() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.get("/role/3/3"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void deleteRoleFromUser_anonymous_return302() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.get("/role/1/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user1", roles = "ADMIN")
    public void addRoleFromUser_admin_returnRedirect() throws Exception {

        when(userService.getUserById(2)).thenReturn(user2);
        when(roleService.getRoleById(2)).thenReturn(managerUser);

        mockMvc
                .perform(MockMvcRequestBuilders.put("/role/2/2"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/role/2"));

        verify(userService, times(1)).getUserById(2);
        verify(roleService, times(1)).getRoleById(2);
        verify(userService, times(1)).updateUser(2, user2);
        assertEquals(1, user2.getRoles().size());
    }

    @Test
    @WithMockUser(username = "user2", roles = "MANAGER_USER")
    public void addRoleFromUser_managerUser_return403() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.get("/role/2/1"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user3", roles = "MANAGER_STATION")
    public void addRoleFromUser_managerStation_return403() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.get("/role/3/1"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void addRoleFromUser_anonymous_return302() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.get("/role/1/2"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }
}