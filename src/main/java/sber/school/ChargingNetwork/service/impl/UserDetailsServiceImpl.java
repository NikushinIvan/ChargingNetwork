package sber.school.ChargingNetwork.service.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sber.school.ChargingNetwork.service.UserService;

import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            var user = userService.getUserByUsername(username);
            Collection<GrantedAuthority> authorities = new HashSet<>();
            user.getRoles().forEach(
                    role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName()))
            );
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .accountExpired(false)
                    .accountLocked(false)
                    .disabled(false)
                    .authorities(authorities)
                    .build();
        } catch (NoSuchElementException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
