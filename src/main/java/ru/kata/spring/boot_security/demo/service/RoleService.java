package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    Role findById(Long id);

    List<Role> findAll();

    Role findByName(String name);

    Set<Role> findRolesByNameIn(Set<String> roleNames);

    void save(Role role);

    boolean existsByName(String name);

    Set<Role> findRolesByIds(Set<Long> ids);
}