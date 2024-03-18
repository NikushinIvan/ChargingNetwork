package sber.school.ChargingNetwork.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import sber.school.ChargingNetwork.model.user.Role;
import sber.school.ChargingNetwork.model.user.User;
import sber.school.ChargingNetwork.service.RoleService;
import sber.school.ChargingNetwork.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    @Mock
    private RoleService roleService;
    @Mock
    private UserService userService;

    @InjectMocks
    private RoleController roleController;

    @Test
    void showUpdateUserRolePage_validRequest_returnViewName() {
        var model = Mockito.mock(Model.class);
        var id = 1;
        var user = new User(1, "email", "Иван", "Фролов", null);
        var roles = List.of(new Role());

        doReturn(user).when(userService).getUserById(1);
        doReturn(roles).when(roleService).excludeRepeatRoles(user.getRoles());

        var response = roleController.showUpdateUserRolePage(id, model);

        verify(userService, times(1)).getUserById(1);
        verify(model, times(1)).addAttribute("user", user);
        verify(model, times(1)).addAttribute("roles", roles);
        assertEquals("/role/updateUserRole", response);
    }

    @Test
    void showUpdateUserRolePage_unknownId_throwNoSuchElementException() {
        var model = Mockito.mock(Model.class);
        var id = 2;

        doThrow(NoSuchElementException.class).when(userService).getUserById(2);

        assertThrows(NoSuchElementException.class, () -> roleController.showUpdateUserRolePage(id, model));

        verify(userService, times(1)).getUserById(2);
        verify(model, never()).addAttribute(anyString(), any());
        verify(model, never()).addAttribute(anyString(), anyCollection());
    }

    @Test
    void deleteRoleFromUser_validRequest_returnViewName() {
        var user = new User(1, "email", "Иван", "Фролов", null);
        var role = new Role(1, "ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        doReturn(user).when(userService).getUserById(1);
        doReturn(role).when(roleService).getRoleById(1);

        var response = roleController.deleteRoleFromUser(1, 1);

        verify(userService, times(1)).getUserById(1);
        verify(roleService, times(1)).getRoleById(1);
        verify(userService, times(1)).updateUser(1, user);
        assertEquals(0, user.getRoles().size());
        assertEquals("redirect:/role/1", response);
    }

    @Test
    void deleteRoleFromUser_invalidUserId_throwNoSuchElementException() {

        doThrow(NoSuchElementException.class).when(userService).getUserById(2);

        assertThrows(NoSuchElementException.class, () -> roleController.deleteRoleFromUser(2, 2));

        verify(userService, times(1)).getUserById(2);
        verify(roleService, never()).getRoleById(anyInt());
        verify(userService, never()).updateUser(anyInt(), any());
    }

    @Test
    void deleteRoleFromUser_invalidRoleId_throwNoSuchElementException() {
        var user = new User(3, "email", "Иван", "Фролов", null);

        doReturn(user).when(userService).getUserById(3);
        doThrow(NoSuchElementException.class).when(roleService).getRoleById(3);

        assertThrows(NoSuchElementException.class, () -> roleController.deleteRoleFromUser(3, 3));

        verify(userService, times(1)).getUserById(3);
        verify(roleService, times(1)).getRoleById(3);
        verify(userService, never()).updateUser(anyInt(), any());
    }

    @Test
    void addRoleFromUser_validRequest_returnViewName() {
        var user = new User(4, "email", "Иван", "Фролов", null);
        var role = new Role(4, "ADMIN");
        user.setRoles(new HashSet<>());

        doReturn(user).when(userService).getUserById(4);
        doReturn(role).when(roleService).getRoleById(4);

        var response = roleController.addRoleFromUser(4, 4);

        verify(userService, times(1)).getUserById(4);
        verify(roleService, times(1)).getRoleById(4);
        verify(userService, times(1)).updateUser(4, user);
        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().contains(role));
        assertEquals("redirect:/role/4", response);
    }

    @Test
    void addRoleFromUser_invalidUserId_throwNoSuchElementException() {

        doThrow(NoSuchElementException.class).when(userService).getUserById(5);

        assertThrows(NoSuchElementException.class, () -> roleController.addRoleFromUser(5, 5));

        verify(userService, times(1)).getUserById(5);
        verify(roleService, never()).getRoleById(anyInt());
        verify(userService, never()).updateUser(anyInt(), any());
    }

    @Test
    void addRoleFromUser_invalidRoleId_throwNoSuchElementException() {
        var user = new User(6, "email", "Иван", "Фролов", null);

        doReturn(user).when(userService).getUserById(6);
        doThrow(NoSuchElementException.class).when(roleService).getRoleById(6);

        assertThrows(NoSuchElementException.class, () -> roleController.addRoleFromUser(6, 6));

        verify(userService, times(1)).getUserById(6);
        verify(roleService, times(1)).getRoleById(6);
        verify(userService, never()).updateUser(anyInt(), any());
    }
}