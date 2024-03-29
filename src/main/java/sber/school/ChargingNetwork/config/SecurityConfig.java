package sber.school.ChargingNetwork.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import sber.school.ChargingNetwork.service.ApplicationStationDetailsService;
import sber.school.ChargingNetwork.service.ApplicationUserDetailsService;

import static org.springframework.http.HttpMethod.GET;

/**
 *
 * Конфигурациооный класс контекста безопасности приложения
 *
 */
@Configuration
public class SecurityConfig {

    private final ApplicationUserDetailsService userDetailsService;
    private final ApplicationStationDetailsService stationDetailsService;

    public SecurityConfig(ApplicationUserDetailsService userDetailsService,
                          ApplicationStationDetailsService stationDetailsService) {
        this.userDetailsService = userDetailsService;
        this.stationDetailsService = stationDetailsService;
    }

    /**
     *
     * Метод аутентификиции и авторизации зарядных станций
     *
     * @param http - Объект настройки веб-безопасности для определенных HTTP-запросов
     * @return Цепочка фильтров для аутентификации и авторизации зарядных станций
     * @throws Exception
     */
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

    /**
     *
     * Метод аутентификиции и авторизации пользователей
     *
     * @param http - Объект настройки веб-безопасности для определенных HTTP-запросов
     * @return Цепочка фильтров для аутентификации и авторизации пользователей
     * @throws Exception
     */
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
                    .mvcMatchers("/login", "/error", "/logout", "/css/**").permitAll().and()
                .authorizeRequests()
                    .regexMatchers(GET, "/user/(\\d+)").authenticated()
                    .antMatchers("/user").hasAnyRole("ADMIN", "MANAGER_USER")
                    .mvcMatchers("/user/**").hasAnyRole("ADMIN", "MANAGER_USER")
                    .mvcMatchers("/role/**").hasRole("ADMIN").and()
                .authorizeRequests()
                    .antMatchers(GET, "/station").authenticated()
                    .mvcMatchers("/station/**").hasAnyRole("ADMIN", "MANAGER_STATION").and()
                .authorizeRequests()
                    .antMatchers(GET, "/").authenticated()
                    .anyRequest().denyAll().and()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/").and()
                .build();
    }

    /**
     *
     * Метод создает бин кодировщика пароля.
     *
     * @return Объект класса, реализующий интерфейс PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
