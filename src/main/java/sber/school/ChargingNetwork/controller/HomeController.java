package sber.school.ChargingNetwork.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sber.school.ChargingNetwork.model.user.Role;
import sber.school.ChargingNetwork.service.UserService;

import java.util.stream.Collectors;

/**
 *
 * Контроллер домашней страницы
 *
 */
@Controller
@RequestMapping("/")
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    /**
     *
     * Метод обрабатывает GET запросы к корневому URL
     *
     * @param model - Передает в представление данные пользователя.
     *              Атрибут user - объект пользователя,
     *              Атрибут roles - описания ролей данного пользователя
     * @param userDetails - данные аутентификации пользователя, который обращается к странице
     * @return Строка с названием представления
     */
    @GetMapping
    public String getHomePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.getUserByUsername(userDetails.getUsername());
        var roles = user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList());
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "/home";
    }

}
