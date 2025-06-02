package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Set;

@Controller
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    // Аутентификация
    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login";
    }

    // Админ-панель
    @GetMapping("/admin")
    public String showAdminPanel(Model model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    // Добавление пользователя
    @GetMapping("/admin/new")
    public String showNewUserForm(Model model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAll()); // Используем findAll()
        return "admin/new-user";
    }

    @PostMapping("/admin/new")
    public String createUser(@ModelAttribute("user") User user,
                             BindingResult result,
                             Principal principal,
                             Model model) {

        if (userService.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "Username already exists");
        }

        if (result.hasErrors()) {
            model.addAttribute("currentUser", userService.findByUsername(principal.getName()));
            model.addAttribute("allRoles", roleService.findAll());
            return "admin/new-user";
        }

        userService.save(user);
        return "redirect:/admin?success";
    }

    // Редактирование пользователя
    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               Model model,
                               Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        User userToEdit = userService.findById(id);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("user", userToEdit);
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/edit-user";
    }

    @PostMapping("/admin/edit/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute("user") User user,
                             BindingResult result,
                             Principal principal,
                             Model model) {

        User existingUser = userService.findById(id);



        if (result.hasErrors()) {
            model.addAttribute("currentUser", userService.findByUsername(principal.getName()));
            model.addAttribute("allRoles", roleService.findAll());
            return "admin/edit-user";
        }

        userService.update(user);
        return "redirect:/admin?updated";
    }

    // Удаление пользователя
    @GetMapping("/admin/delete/{id}")
    public String showDeleteForm(@PathVariable Long id,
                                 Model model,
                                 Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        User userToDelete = userService.findById(id);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("user", userToDelete);
        return "admin/delete-user";
    }

    @PostMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin?deleted";
    }

    // Профиль пользователя
    @GetMapping("/user")
    public String showUserProfile(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user/profile";
    }
}
