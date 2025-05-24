package ru.kata.spring.boot_security.demo.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;


import javax.annotation.PostConstruct;
import java.util.Collections;


@Component
public class Init {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public Init(UserService userService,
                RoleService roleService,
                PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        initializeRoles();
        initializeAdminUser();
        initializeRegularUser();
    }

    private void initializeRoles() {
        if (roleService.findAll().isEmpty()) {
            Role adminRole = new Role("ROLE_ADMIN");
            Role userRole = new Role("ROLE_USER");
            roleService.save(adminRole);
            roleService.save(userRole);
        }
    }

    private void initializeAdminUser() {
        if (!userService.existsByUsername("admin")) {
            Role adminRole = roleService.findByName("ROLE_ADMIN");
            if (adminRole == null) {
                throw new IllegalStateException("Admin role not found. Initialize roles first!");
            }

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(Collections.singleton(adminRole));
            userService.save(admin);


        }
    }

    private void initializeRegularUser() {
        if (!userService.existsByUsername("user")) {
            Role userRole = roleService.findByName("ROLE_USER");
            if (userRole == null) {
                throw new IllegalStateException("User role not found. Initialize roles first!");
            }

            User regularUser = new User();
            regularUser.setUsername("user");
            regularUser.setPassword(passwordEncoder.encode("user"));
            regularUser.setEmail("user@example.com");
            regularUser.setRoles(Collections.singleton(userRole));
            userService.save(regularUser);
        }
    }
}