package sber.school.ChargingNetwork.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sber.school.ChargingNetwork.model.user.Role;
import sber.school.ChargingNetwork.repository.RoleRepository;

import java.util.*;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    public void getAllRoles_returnListRoles() {
        var roles = List.of(
                new Role(1, "ROLE_ADMIN"),
                new Role(2, "ROLE_MANAGER_USER")
        );

        doReturn(roles).when(roleRepository).findAll();

        var result = roleService.getAllRoles();

        verify(roleRepository, times(1)).findAll();
        assertAll(
                () -> assertEquals(2, StreamSupport.stream(result.spliterator(), false).count()),
                () -> assertTrue(StreamSupport.stream(result.spliterator(), false)
                        .anyMatch(role -> role.getRoleName().equals("ROLE_ADMIN"))),
                () -> assertTrue(StreamSupport.stream(result.spliterator(), false)
                        .anyMatch(role -> role.getRoleName().equals("ROLE_MANAGER_USER")))
        );
    }

    @Test
    public void getRoleById_validRoleId_returnRole() {
        var role = new Role(2, "ROLE_ADMIN");
        var id = 2;

        doReturn(Optional.of(role)).when(roleRepository).findById(2);

        var result = roleService.getRoleById(id);

        verify(roleRepository, times(1)).findById(2);
        assertAll(
                () -> assertEquals(2, result.getRoleId()),
                () -> assertEquals("ROLE_ADMIN", result.getRoleName())
        );
    }

    @Test
    public void getRoleById_validRoleId_throwNoSuchElementException() {
        var id = 3;

        doThrow(NoSuchElementException.class).when(roleRepository).findById(3);

        assertThrows(NoSuchElementException.class, () -> roleService.getRoleById(id));

        verify(roleRepository, times(1)).findById(3);
    }

    @Test
    public void excludeRepeatRoles_returnRoles() {
        var role1 = new Role(1, "ROLE_ADMIN");
        var role2 = new Role(2, "ROLE_MANAGER_USER");
        var role3 = new Role(3, "ROLE_MANAGER_STATION");
        List<Role> roles = new ArrayList<>(List.of(role1, role2, role3));
        var userRoles = Set.of(role2);

        doReturn(roles).when(roleRepository).findAll();

        var result = roleService.excludeRepeatRoles(userRoles);

        verify(roleRepository, times(1)).findAll();
        assertAll(
                () -> assertEquals(2, StreamSupport.stream(result.spliterator(), false).count()),
                () -> assertTrue(StreamSupport.stream(result.spliterator(), false)
                        .anyMatch(role -> role.getRoleName().equals("ROLE_ADMIN"))),
                () -> assertTrue(StreamSupport.stream(result.spliterator(), false)
                        .anyMatch(role -> role.getRoleName().equals("ROLE_MANAGER_STATION")))
        );
    }
}