package sber.school.ChargingNetwork.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import sber.school.ChargingNetwork.model.station.Address;
import sber.school.ChargingNetwork.model.station.Station;
import sber.school.ChargingNetwork.model.station.Vendor;
import sber.school.ChargingNetwork.model.user.User;
import sber.school.ChargingNetwork.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void getUsers_validRequest_returnViewNameAndListUsers() {
        var model = mock(Model.class);
        var users = List.of(
                new User(1, "email", "Иван", "Фролов", null),
                new User(2, "qwer", "Олег", "Котов", null),
                new User(3, "asdf", "Екатерина", "Бусина", null));

        doReturn(users).when(userService).getUsers();

        var response = userController.getUsers(model);

        verify(userService, times(1)).getUsers();
        verify(model, times(1)).addAttribute("users", users);
        assertEquals("/user/user", response);
    }

    @Test
    public void createUser_validRequest_returnRedirect() {
        var user = new User(1, "user1", "Daria", "Ivanova", null);

        doReturn(user).when(userService).saveUser(user);

        var response = userController.createUser(user);

        verify(userService, times(1)).saveUser(user);
        assertEquals("redirect:/user", response);
    }

    @Test
    public void getUser_validRequest_returnViewNameAndUser() {
        var model = mock(Model.class);
        var id = 2;
        var user = new User(2, "user2", "Михаил", "Никушин", "22233344");
        var userDetails = new org.springframework.security.core.userdetails.User(
                "user2", "password", new HashSet<>());

        doReturn(user).when(userService).getUserById(2);

        var response = userController.getUser(model, id, userDetails);

        verify(userService, times(1)).getUserById(2);
        verify(model, times(1)).addAttribute("user", user);
        assertEquals("/user/profile", response);
    }

    @Test
    public void getUser_anotherUser_returnErrorPageNameAndErrorText() {
        var model = mock(Model.class);
        var id = 3;
        var user = new User(3, "user3", "Михаил", "Никушин", "22233344");
        var userDetails = new org.springframework.security.core.userdetails.User(
                "user2", "password", new HashSet<>());

        doReturn(user).when(userService).getUserById(3);

        var response = userController.getUser(model, id, userDetails);

        verify(userService, times(1)).getUserById(3);
        verify(model, times(1)).addAttribute("error", "Страница данного пользователя недоступна");
        assertEquals("error", response);
    }

    @Test
    public void deleteUser_validRequest_returnRedirect() {
        var id = 4;

        doNothing().when(userService).deleteUser(4);

        var response = userController.deleteUser(id);

        verify(userService, times(1)).deleteUser(4);
        assertEquals("redirect:/user", response);
    }

    @Test
    public void updateUser_validRequest_returnRedirect() {
        var user = new User(0, "user4", "Михаил", "Крамов", "22233344");
        var id = 5;

        doNothing().when(userService).updateUser(5, user);

        var response = userController.updateUser(id, user);

        verify(userService, times(1)).updateUser(5, user);
        assertEquals("redirect:/user", response);
    }

    @Test
    public void showManagerPanel_validRequest_returnViewName() {

        assertEquals("/user/managerPanel", userController.showManagerPanel());

    }

    @Test
    public void showCreatePage_validRequest_returnViewName() {
        var model = mock(Model.class);

        var response = userController.showCreatePage(model);

        verify(model, times(1)).addAttribute(eq("user"), any(User.class));
        assertEquals("/user/create", response);
    }

    @Test
    public void showUpdatePage_validRequest_returnViewName() {
        var model = mock(Model.class);
        var id = 6;
        var user = new User(6, "user6", "Михаил", "Крамов", "22233344");

        doReturn(user).when(userService).getUserById(6);


        var response = userController.showUpdatePage(model, id);

        verify(userService, times(1)).getUserById(6);
        verify(model, times(1)).addAttribute("user", user);
        assertEquals("/user/update", response);
    }

    @Test
    public void handleNoSuchElementException_noSuchElementException() {
        var model = mock(Model.class);
        var httpResponse = mock(HttpServletResponse.class);
        var exception = mock(NoSuchElementException.class);

        doReturn("Пользователь не найден").when(exception).getMessage();

        var response = userController.handleNoSuchElementException(exception, model, httpResponse);

        verify(exception, times(1)).getMessage();
        verify(model, times(1)).addAttribute("error", "Пользователь не найден");
        verify(httpResponse, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
        assertEquals("error", response);
    }
}