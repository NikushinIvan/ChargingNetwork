package sber.school.ChargingNetwork.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import sber.school.ChargingNetwork.model.station.Station;
import sber.school.ChargingNetwork.model.user.Role;
import sber.school.ChargingNetwork.model.user.User;
import sber.school.ChargingNetwork.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ApplicationUserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ApplicationUserDetailsServiceImpl userDetailsService;

    @Test
    public void loadUserByUsername_validUsername_returnUser() {
        var username = "user1";
        var user = new User(1, "user1", "password1");
        var roles = Set.of(new Role(1, "ROLE_ADMIN"));
        user.setRoles(roles);
        var authority = new SimpleGrantedAuthority("ROLE_ADMIN");

        doReturn(user).when(userRepository).findByUsername("user1");

        var userDetails = userDetailsService.loadUserByUsername(username);

        verify(userRepository, times(1)).findByUsername("user1");
        assertAll(
                () -> assertEquals("user1", userDetails.getUsername()),
                () -> assertEquals("password1", userDetails.getPassword()),
                () -> assertEquals(1, userDetails.getAuthorities().size()),
                () -> assertTrue(userDetails.getAuthorities().contains(authority))
        );
    }

    @Test
    public void loadUserByUsername_invalidUsername_throwUsernameNotFoundException() {
        var username = "user2";

        doThrow(NoSuchElementException.class).when(userRepository).findByUsername("user2");

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));

        verify(userRepository, times(1)).findByUsername("user2");
    }
}