package ru.kata.spring.boot_security.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {
    private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);
    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    @Transactional(readOnly = true)
    public Role findById(Long id) {
        log.debug("Finding role by id: {}", id);
        return roleDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Role> findRolesByIds(Set<Long> ids) {
        return roleDao.findAllByIdIn(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAll() {
        log.debug("Retrieving all roles");
        return roleDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Role findByName(String name) {
        log.debug("Finding role by name: {}", name);
        return roleDao.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Role> findRolesByNameIn(Set<String> roleNames) {
        log.debug("Finding roles by names: {}", roleNames);
        return roleDao.findByNames(roleNames);
    }

    @Override
    @Transactional
    public void save(Role role) {
        roleDao.save(role);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return roleDao.existsByName(name);
    }
}