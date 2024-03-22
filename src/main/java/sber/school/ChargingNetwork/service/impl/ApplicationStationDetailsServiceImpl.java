package sber.school.ChargingNetwork.service.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sber.school.ChargingNetwork.repository.StationRepository;
import sber.school.ChargingNetwork.service.ApplicationStationDetailsService;

import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;

/**
 *
 * Класс реализует UserDetailsService для аутентификации зарядных станций
 *
 */
@Service
public class ApplicationStationDetailsServiceImpl implements ApplicationStationDetailsService {

    private final StationRepository stationRepository;

    public ApplicationStationDetailsServiceImpl(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            var user = stationRepository.findByLogin(username);
            Collection<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_STATION"));
            return User.builder()
                    .username(user.getLogin())
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
