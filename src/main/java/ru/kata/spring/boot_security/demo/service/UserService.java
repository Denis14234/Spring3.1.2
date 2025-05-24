package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    User findById(Long id);
    List<User> findAll();
    User findByUsername(String username);
    void save(User user);
    void update(User user);
    void delete(Long id);
    boolean existsByUsername(String username);
}
