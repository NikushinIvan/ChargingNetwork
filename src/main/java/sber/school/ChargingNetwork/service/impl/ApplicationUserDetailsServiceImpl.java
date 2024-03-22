package sber.school.ChargingNetwork.service.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sber.school.ChargingNetwork.repository.UserRepository;
import sber.school.ChargingNetwork.service.ApplicationUserDetailsService;

import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;

/**
 *
 * Класс реализует UserDetailsService для аутентификации пользователей
 *
 */
@Service
public class ApplicationUserDetailsServiceImpl implements ApplicationUserDetailsService {

    private final UserRepository userRepository;

    public ApplicationUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            var user = userRepository.findByUsername(username);
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
