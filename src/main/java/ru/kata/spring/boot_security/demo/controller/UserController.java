package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    // Метод для страницы пользователя
    @GetMapping("/user")
    public String showUserProfile(Model model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        model.addAttribute("user", currentUser);
        return "user";
    }

    // Методы для админ-панели
    @GetMapping("/admin")
    public String showAdminPanel(Model model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/admin/new")
    public String showNewUserForm(Model model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/new-user";
    }

    @PostMapping("/admin/new")
    public String createUser(@Valid @ModelAttribute("user") User user,
                             @RequestParam("roles") Set<Long> roleIds,
                             BindingResult result,
                             Model model,
                             Principal principal) {

        if (userService.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "Username already exists");
        }

        if (result.hasErrors()) {
            model.addAttribute("currentUser", userService.findByUsername(principal.getName()));
            model.addAttribute("allRoles", roleService.findAll());
            return "admin/new-user";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roleService.findRolesByIds(roleIds));
        userService.save(user);

        return "redirect:/admin?success";
    }

    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        User userToEdit = userService.findById(id);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("user", userToEdit);
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/edit-user";
    }

    @PostMapping("/admin/edit/{id}")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute("user") User user,
                             @RequestParam("roles") Set<Long> roleIds,
                             BindingResult result,
                             Model model,
                             Principal principal) {

        User existingUser = userService.findById(id);

        if (!existingUser.getUsername().equals(user.getUsername()) &&
                userService.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "Username already exists");
        }

        if (result.hasErrors()) {
            model.addAttribute("currentUser", userService.findByUsername(principal.getName()));
            model.addAttribute("allRoles", roleService.findAll());
            return "admin/edit-user";
        }

        if (user.getPassword().isEmpty()) {
            user.setPassword(existingUser.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        user.setRoles(roleService.findRolesByIds(roleIds));
        user.setId(id);
        userService.update(user);

        return "redirect:/admin?updated";
    }

    @GetMapping("/admin/delete/{id}")
    public String showDeleteForm(@PathVariable Long id, Model model, Principal principal) {
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
}
