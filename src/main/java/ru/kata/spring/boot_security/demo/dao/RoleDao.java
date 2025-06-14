package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleDao {
    Role findById(Long id);

    List<Role> findAll();

    Role findByName(String name);

    Set<Role> findByNames(Set<String> names);

    void save(Role role);

    boolean existsByName(String name);

    Set<Role> findAllByIdIn(Set<Long> ids);
}