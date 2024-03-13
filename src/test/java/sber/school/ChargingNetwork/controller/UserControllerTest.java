package sber.school.ChargingNetwork.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sber.school.ChargingNetwork.model.user.User;
import sber.school.ChargingNetwork.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private static User user;
    private static User user1;
    private static User user2;

    @BeforeAll
    static void beforeAll() {
        user = new User(1, "user1", "Daria", "Ivanova", null);
        user1 = new User(2, "user2", "Ivan", "Petrov", null);
        user2 = new User("user2", "Иван", "Никитин", null);
    }

    @Test
    public void showAllUsers() throws Exception {
        when(userService.getUsers()).thenReturn(List.of(user, user1));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/user/user"))
                .andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.contains(user, user1)));
    }

    @Test
    public void showUser() throws Exception {
        when(userService.getUserById(1)).thenReturn(user);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/user/profile"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", Matchers.equalToObject(user)));
    }

    @Test
    public void showUnknownUser() throws Exception {
        when(userService.getUserById(3)).thenThrow(NoSuchElementException.class);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/3"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.view().name("error"))
                .andExpect(MockMvcResultMatchers.model().attribute(
                        "errorText",
                        Matchers.equalToObject("Пользователь не найден")));
    }

    @Test
    public void showCreatePageTest() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/user/create"));
    }

    @Test
    public void createUser() throws Exception {
        when(userService.saveUser(user2)).thenAnswer(
                (i) -> {
                    User argument = i.getArgument(0);
                    argument.setUserId(3);
                    return argument;
                }
        ).thenReturn(user2);

        mockMvc
                .perform(MockMvcRequestBuilders.post("/user").flashAttr("user", user2))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/user"));
    }

    @Test
    public void deleteUser() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/user/3"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/user"));
    }

    @Test
    public void showUpdateUserPage() throws Exception {
        when(userService.getUserById(1)).thenReturn(user);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/1/update"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/user/update"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", Matchers.equalToObject(user)));
    }

    @Test
    public void updateUser() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.put("/user/2"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/user"));
    }
}