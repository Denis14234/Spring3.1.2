package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    void delete(Long id);
    void update(User us);
    boolean add(User user);
    List<User> listUsers();
    User findById(Long id);
    Role findByIdRole(Long id);
    List<Role> listRoles();
    Role findByNameRole(String name);
    List<Role> listByRole(List<String> name);
    boolean addRole(Role role);
}
