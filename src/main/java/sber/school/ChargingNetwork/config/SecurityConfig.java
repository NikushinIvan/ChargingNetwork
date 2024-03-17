package sber.school.ChargingNetwork.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import sber.school.ChargingNetwork.service.ApplicationStationDetailsService;
import sber.school.ChargingNetwork.service.ApplicationUserDetailsService;

import static org.springframework.http.HttpMethod.GET;

@Configuration
public class SecurityConfig {

    private final ApplicationUserDetailsService userDetailsService;
    private final ApplicationStationDetailsService stationDetailsService;

    public SecurityConfig(ApplicationUserDetailsService userDetailsService,
                          ApplicationStationDetailsService stationDetailsService) {
        this.userDetailsService = userDetailsService;
        this.stationDetailsService = stationDetailsService;
    }


    @Bean
    @Order(1)
    public SecurityFilterChain chargerSecurityFilterChain(HttpSecurity http) throws Exception {
        var authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(stationDetailsService)
                .passwordEncoder(passwordEncoder());
        return http
                .mvcMatcher("/charger/**")
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().anyRequest().hasRole("STATION").and()
                .httpBasic().and()
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
        var authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return http
                .mvcMatcher("/**")
                .csrf().disable()
                .authorizeRequests()
                    .mvcMatchers("/login", "/error", "/logout").permitAll().and()
                .authorizeRequests()
                    .antMatchers(GET, "/user/*").authenticated()
                    .antMatchers("/user").hasAnyRole("ADMIN", "MANAGER_USER")
                    .mvcMatchers("/user/**").hasAnyRole("ADMIN", "MANAGER_USER")
                    .mvcMatchers("/role/**").hasRole("ADMIN").and()
                .authorizeRequests()
                    .antMatchers(GET, "/station").authenticated()
                    .mvcMatchers("/station/**").hasAnyRole("ADMIN", "MANAGER_STATION").and()
                .authorizeRequests()
                    .anyRequest().denyAll().and()
                .formLogin().and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
