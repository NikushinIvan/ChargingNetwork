package sber.school.ChargingNetwork.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import sber.school.ChargingNetwork.model.user.User;
import sber.school.ChargingNetwork.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void saveUser() {
        var user = mock(User.class);

        doReturn("string").when(user).getPassword();
        doReturn("EncodingString").when(passwordEncoder).encode("string");

        userService.saveUser(user);

        verify(user, times(1)).getPassword();
        verify(passwordEncoder, times(1)).encode("string");
        verify(user, times(1)).setPassword("EncodingString");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void getUsers_returnListUsers() {
        var user1 = new User("11223344");
        var user2 = new User("01122334");
        Iterable<User> users = List.of(user1, user2);

        doReturn(users).when(userRepository).findAll();

        var result = userService.getUsers();

        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.contains(user1)),
                () -> assertTrue(result.contains(user2))
        );
    }

    @Test
    public void getUserById_validUserId_returnUser() {
        var id = 1;
        var user = mock(User.class);

        doReturn(Optional.of(user)).when(userRepository).findById(1);

        var result = userService.getUserById(id);

        verify(userRepository).findById(1);
        assertEquals(user, result);
    }

    @Test
    public void getUserById_invalidUserId_throwNoSuchElementException() {
        var id = 2;

        doReturn(Optional.empty()).when(userRepository).findById(2);

        assertThrows(NoSuchElementException.class, () -> userService.getUserById(id));

        verify(userRepository).findById(2);
    }

    @Test
    public void updateUser() {
        var id = 3;
        var updatedUser = mock(User.class);
        var user = new User();

        doReturn(Optional.of(user)).when(userRepository).findById(3);

        userService.updateUser(id, updatedUser);

        verify(userRepository, times(1)).findById(3);
        verify(updatedUser, times(1)).setRoles(null);
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    public void deleteUser() {
        var id = 4;

        userService.deleteUser(id);

        verify(userRepository, times(1)).deleteById(4);
    }

    @Test
    public void getUsersGetWithRole() {
        var roleName = "ROLE_ADMIN";
        var users = List.of(
                new User("11223344"),
                new User("00112233")
        );

        doReturn(users).when(userRepository).findByRoles_RoleNameContaining("ROLE_ADMIN");

        assertEquals(users, userService.getUsersGetWithRole(roleName));
        verify(userRepository, times(1)).findByRoles_RoleNameContaining("ROLE_ADMIN");
    }

    @Test
    public void getUserByUid_validUid_returnUser() {
        var uid = "11223344";
        var user = mock(User.class);

        doReturn(Optional.of(user)).when(userRepository).findByUid("11223344");

        var result = userService.getUserByUid(uid);

        verify(userRepository).findByUid("11223344");
        assertEquals(user, result);
    }

    @Test
    public void getUserByUid_invalidUid_throwNoSuchElementException() {
        var uid = "00001111";

        doReturn(Optional.empty()).when(userRepository).findByUid("00001111");

        assertThrows(NoSuchElementException.class, () -> userService.getUserByUid(uid));

        verify(userRepository).findByUid("00001111");
    }

    @Test
    public void getUserByFirstNameAndLastName_validData_returnUser() {
        var firstName = "Иван";
        var lastName = "Петров";
        var user = mock(User.class);

        doReturn(Optional.of(user)).when(userRepository).findByFirstNameAndLastName("Иван", "Петров");

        var result = userService.getUserByFirstNameAndLastName(firstName, lastName);

        verify(userRepository).findByFirstNameAndLastName("Иван", "Петров");
        assertEquals(user, result);
    }

    @Test
    public void getUserByFirstNameAndLastName_invalidData_throwNoSuchElementException() {
        var firstName = "Иван";
        var lastName = "Серый";

        doReturn(Optional.empty()).when(userRepository).findByFirstNameAndLastName("Иван", "Серый");

        assertThrows(NoSuchElementException.class,
                () -> userService.getUserByFirstNameAndLastName(firstName, lastName));

        verify(userRepository).findByFirstNameAndLastName("Иван", "Серый");
    }

    @Test
    public void getUserByUserName_validUsername_returnUser() {
        var username = "user1";
        var user = mock(User.class);

        doReturn(user).when(userRepository).findByUsername("user1");

        var result = userService.getUserByUsername(username);

        verify(userRepository).findByUsername("user1");
        assertEquals(user, result);
    }

    @Test
    public void getUserByUserName_invalidUsername_throwNoSuchElementException() {
        var username = "user2";
        var user = mock(User.class);

        doThrow(NoSuchElementException.class).when(userRepository).findByUsername("user2");

        assertThrows(NoSuchElementException.class, () -> userService.getUserByUsername(username));

        verify(userRepository).findByUsername("user2");
    }
}