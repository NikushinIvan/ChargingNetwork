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
import sber.school.ChargingNetwork.model.user.User;
import sber.school.ChargingNetwork.service.ApplicationStationDetailsService;
import sber.school.ChargingNetwork.service.ApplicationUserDetailsService;
import sber.school.ChargingNetwork.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerIntegrationTest {

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
        user2 = new User(2, "user2", "Ivan", "Petrov", null);
        user3 = new User("user3", "Иван", "Никитин", null);
    }

    @Test
    @WithMockUser(username = "user1", roles = "ADMIN")
    public void showAllUsers_admin_returnOk() throws Exception {
        when(userService.getUsers()).thenReturn(List.of(user1, user2));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/user/user"))
                .andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.contains(user1, user2)));
    }

    @Test
    @WithMockUser(username = "user2", roles = "MANAGER_USER")
    public void showAllUsers_managerUser_returnOk() throws Exception {
        when(userService.getUsers()).thenReturn(List.of(user1, user2));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/user/user"))
                .andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.contains(user1, user2)));
    }

    @Test
    @WithMockUser(username = "user3", roles = "MANAGER_STATION")
    public void showAllUsers_managerStation_return403() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void showAllUsers_anonymous_return302() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user1", roles = "ADMIN")
    public void createUser_admin_returnRedirect() throws Exception {
        when(userService.saveUser(user3)).thenAnswer(
                (i) -> {
                    User argument = i.getArgument(0);
                    argument.setUserId(3);
                    return argument;
                }
        );

        mockMvc
                .perform(MockMvcRequestBuilders.post("/user").flashAttr("user", user3))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/user"));
    }

    @Test
    @WithMockUser(username = "user2", roles = "MANAGER_USER")
    public void createUser_managerUser_returnRedirect() throws Exception {
        when(userService.saveUser(user3)).thenAnswer(
                (i) -> {
                    User argument = i.getArgument(0);
                    argument.setUserId(3);
                    return argument;
                }
        );

        mockMvc
                .perform(MockMvcRequestBuilders.post("/user").flashAttr("user", user3))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/user"));
    }

    @Test
    @WithMockUser(username = "user3", roles = "MANAGER_STATION")
    public void createUser_managerStation_return403() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.post("/user").flashAttr("user", user3))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void createUser_anonymous_return302() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.post("/user"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }


    @Test
    @WithMockUser(username = "user1", roles = "ADMIN")
    public void getUser_admin_returnOk() throws Exception {
        when(userService.getUserById(1)).thenReturn(user1);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/user/profile"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", Matchers.equalToObject(user1)));
    }

    @Test
    @WithMockUser(username = "user1", roles = "ADMIN")
    public void getUser_adminAnotherProfile_returnOk() throws Exception {
        when(userService.getUserById(2)).thenReturn(user2);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/2"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }

    @Test
    @WithMockUser(username = "user2", roles = "MANAGER_USER")
    public void getUser_managerUser_returnOk() throws Exception {
        when(userService.getUserById(2)).thenReturn(user2);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/user/profile"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", Matchers.equalToObject(user2)));
    }

    @Test
    @WithMockUser(username = "user3", roles = "MANAGER_STATION")
    public void getUser_managerStation_returnOk() throws Exception {
        when(userService.getUserById(3)).thenReturn(user3);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/3"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/user/profile"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", Matchers.equalToObject(user3)));
    }

    @Test
    @WithAnonymousUser
    public void getUser_anonymous_return302() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user1", roles = "ADMIN")
    public void deleteUser_admin_returnRedirect() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/user/3"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/user"));
    }

    @Test
    @WithMockUser(username = "user2", roles = "MANAGER_USER")
    public void deleteUser_managerUser_returnRedirect() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/user/3"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/user"));
    }

    @Test
    @WithMockUser(username = "user3", roles = "MANAGER_STATION")
    public void deleteUser_managerStation_return403() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.delete("/user/3"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void deleteUser_anonymous_return302() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.delete("/user/3"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user1", roles = "ADMIN")
    public void updateUser_admin_returnRedirect() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.put("/user/3"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user2", roles = "MANAGER_USER")
    public void updateUser_managerUser_returnRedirect() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.put("/user/3"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user3", roles = "MANAGER_STATION")
    public void updateUser_managerStation_return403() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.put("/user/3"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void updateUser_anonymous_return302() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.put("/user/3"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user1", roles = "ADMIN")
    public void showManagerPanel_admin_returnViewName() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/managerPanel"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/user/managerPanel"));
    }

    @Test
    @WithMockUser(username = "user2", roles = "MANAGER_USER")
    public void showManagerPanel_managerUser_returnViewName() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/managerPanel"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/user/managerPanel"));
    }

    @Test
    @WithMockUser(username = "user3", roles = "MANAGER_STATION")
    public void showManagerPanel_managerStation_returnViewName() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/managerPanel"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void showManagerPanel_anonymous_return302() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.put("/user/managerPanel"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user1", roles = "ADMIN")
    public void showCreatePage_admin_returnViewName() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/user/create"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", Matchers.any(User.class)));
    }

    @Test
    @WithMockUser(username = "user2", roles = "MANAGER_USER")
    public void showCreatePage_managerUser_returnViewName() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/user/create"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", Matchers.any(User.class)));
    }

    @Test
    @WithMockUser(username = "user3", roles = "MANAGER_STATION")
    public void showCreatePage_managerStation_returnViewName() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/create"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void showCreatePage_anonymous_return302() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.put("/user/create"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user1", roles = "ADMIN")
    public void showUpdatePage_admin_returnViewName() throws Exception {
        when(userService.getUserById(2)).thenReturn(user2);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/2/update"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/user/update"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", user2));
    }

    @Test
    @WithMockUser(username = "user2", roles = "MANAGER_USER")
    public void showUpdatePage_managerUser_returnViewName() throws Exception {
        when(userService.getUserById(3)).thenReturn(user3);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/3/update"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/user/update"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", user3));
    }

    @Test
    @WithMockUser(username = "user3", roles = "MANAGER_STATION")
    public void showUpdatePage_managerStation_returnViewName() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/3/update"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void showUpdatePage_anonymous_return302() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.put("/user/3/update"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }
}