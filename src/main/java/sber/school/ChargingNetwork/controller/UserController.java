package sber.school.ChargingNetwork.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sber.school.ChargingNetwork.model.user.User;
import sber.school.ChargingNetwork.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;

/**
 *
 * Контроллер пользователей
 *
 */
@Controller
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     *
     * Метод отображения HTML-страницы со списком всех пользователей
     *
     * @param model - Передает в представление список объектов пользователей.
     *              Атрибут users - список пользователей
     * @return Строка с названием представления
     */
    @GetMapping
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "/user/user";
    }

    /**
     *
     * Метод создания нового пользователя
     *
     * @param user - Объект пользователя, переданный представлением контроллеру
     * @return Перенаправление на страницу со списком пользователей
     */
    @PostMapping
    public String createUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/user";
    }

    /**
     *
     * Метод отображения HTML-страницы профиля пользователя
     *
     * @param model - Передает в представление объект пользователя или сообщение ошибки.
     *              Атрибут user - объект пользователя,
     *              Атрибут error - сообщение ошибки
     * @param id - Содержится в URL, ID пользователя
     * @param userDetails - данные аутентификации пользователя, который обращается к странице
     * @param response - Используется для установки статуса 403(FORBIDDEN) при попытке несанкционированного доступа
     * @return Строка с названием представления
     */
    @GetMapping("/{id}")
    public String getUser(Model model, @PathVariable int id, @AuthenticationPrincipal UserDetails userDetails,
                          HttpServletResponse response) {
        var user = userService.getUserById(id);
        if (userDetails.getUsername().equals(user.getUsername())) {
            model.addAttribute("user", user);
            return "/user/profile";
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            model.addAttribute("error", "Страница данного пользователя недоступна");
            return "error";
        }
    }

    /**
     *
     * Метод удаления пользователя
     *
     * @param id - Содержится в URL, ID пользователя
     * @return Перенаправление на страницу со списком пользователей
     */
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return "redirect:/user";
    }

    /**
     *
     * Метод обновления данных пользователя
     *
     * @param id - Содержится в URL, ID пользователя
     * @param user - Объект пользователя, переданный представлением контроллеру, содержит обновленные данные
     * @return Перенаправление на страницу со списком пользователей
     */
    @PutMapping("/{id}")
    public String updateUser(@PathVariable int id, @ModelAttribute User user) {
        userService.updateUser(id, user);
        return "redirect:/user";
    }

    /**
     *
     * Метод отображения HTML-страницы панели управления пользователями
     *
     * @return Строка с названием представления
     */
    @GetMapping("/managerPanel")
    public String showManagerPanel() {
        return "/user/managerPanel";
    }

    /**
     *
     * Метод отображения HTML-страницы создания пользователя
     *
     * @param model - Передает в представление данные, необходимые для создания нового пользователя.
     *              Атрибут user - пустой объект пользователя
     * @return Строка с названием представления
     */
    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("user", new User());
        return "/user/create";
    }

    /**
     *
     * Метод отображения HTML-страницы обновления данных пользователя
     *
     * @param model - Передает в представление актуальные данные пользователя.
     *              Атрибут user - объект пользователя с актуальными данными
     * @param id - Содержится в URL, ID пользователя
     * @return Строка с названием представления
     */
    @GetMapping("/{id}/update")
    public String showUpdatePage(Model model, @PathVariable int id) {
        var user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "/user/update";
    }

    /**
     *
     * Метод отображения HTML-страницы при выбрасывании исключения NoSuchElementException
     *
     * @param exception - Объект исключения
     * @param model - Передает в представление сообщение об ошибке.
     *              Атрибут error - сообщение об ошибке
     * @param response - Используется для установки статуса 404(NOT_FOUND)
     * @return Строка с названием представления
     */
    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException exception, Model model,
                                               HttpServletResponse response) {
        model.addAttribute("error", exception.getMessage());
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return "error";
    }
}
