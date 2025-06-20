package ru.kata.spring.boot_security.demo.dao;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {
    User findById(Long id);

    List<User> findAll();

    User findByUsername(String username);

    void save(User user);

    void update(User user);

    void delete(Long id);

    boolean existsByUsername(String username);
}