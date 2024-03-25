package sber.school.ChargingNetwork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sber.school.ChargingNetwork.service.RoleService;
import sber.school.ChargingNetwork.service.UserService;

/**
 *
 * Контроллер ролей
 *
 */
@Controller
@RequestMapping("role")
public class RoleController {

    private final RoleService roleService;
    private final UserService userService;

    public RoleController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    /**
     *
     * Метод передает информацию о ролях пользователя
     *
     * @param id - Содержится в URL, ID станции
     * @param model - Передает в представление данные пользователя.
     *               Атрибут user - объект пользователя,
     *               Атрибут roles - объекты ролей данного пользователя
     * @return Строка с названием представления
     */
    @GetMapping("/{id}")
    public String showUpdateUserRolePage(@PathVariable int id, Model model) {
        var user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.excludeRepeatRoles(user.getRoles()));
        return "/role/updateUserRole";
    }

    /**
     *
     * Метод удаляет роль из списка ролей пользователя
     *
     * @param userId - Содержится в URL, ID пользователя
     * @param roleId - Содержится в URL, ID роли
     * @return Перенаправление на страницу изменения ролей пользователя
     */
    @DeleteMapping("/{userId}/{roleId}")
    public String deleteRoleFromUser(@PathVariable int userId, @PathVariable int roleId) {
        var user = userService.getUserById(userId);
        var role = roleService.getRoleById(roleId);
        user.getRoles().remove(role);
        userService.updateUser(userId, user);
        return "redirect:/role/" + userId;
    }

    /**
     *
     * Метод добавляет роль к списку ролей пользователя
     *
     * @param userId - Содержится в URL, ID пользователя
     * @param roleId - Содержится в URL, ID роли
     * @return Перенаправление на страницу изменения ролей пользователя
     */
    @PutMapping("/{userId}/{roleId}")
    public String addRoleFromUser(@PathVariable int userId, @PathVariable int roleId) {
        var user = userService.getUserById(userId);
        var role = roleService.getRoleById(roleId);
        user.getRoles().add(role);
        userService.updateUser(userId, user);
        return "redirect:/role/" + userId;
    }
}
