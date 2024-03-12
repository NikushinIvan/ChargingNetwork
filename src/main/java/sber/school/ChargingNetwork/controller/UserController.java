package sber.school.ChargingNetwork.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sber.school.ChargingNetwork.dao.UserDao;
import sber.school.ChargingNetwork.model.user.User;
import sber.school.ChargingNetwork.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    private final UserDao userDao;

    public UserController(UserService userService, UserDao userDao) {
        this.userService = userService;
        this.userDao = userDao;
    }

    @GetMapping
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "/user/user";
    }

    @PostMapping
    public String createUser(@ModelAttribute User user) {
        userService.createUser(user);
        return "redirect:/user";
    }

    @GetMapping("/{ud}")
    public String getUser(Model model, @PathVariable int id) {
        var user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "/user/profile";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return "redirect:/user";
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable int id, @ModelAttribute User user) {
        userService.updateUser(id, user);
        return "redirect:/user";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("user", new User());
        return "/user/create";
    }

    @GetMapping("/{id}/update")
    public String showUpdatePage(Model model, @PathVariable int id) {
        var user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "/user/update";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException exception, Model model,
                                               HttpServletResponse response) {
        model.addAttribute("errorText", exception.getMessage());
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return "error";
    }
}
