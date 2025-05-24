package ru.kata.spring.boot_security.demo.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Role findById(Long id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public List<Role> findAll() {
        return entityManager.createQuery("FROM Role", Role.class)
                .getResultList();
    }

    @Override
    public Role findByName(String name) {
        try {
            return entityManager.createQuery(
                            "SELECT r FROM Role r WHERE r.role = :name", Role.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            return null;
        }
    }

    @Override
    public Set<Role> findByNames(Set<String> names) {
        return entityManager.createQuery(
                        "SELECT r FROM Role r WHERE r.role IN :names", Role.class)
                .setParameter("names", names)
                .getResultStream()
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Role> findAllByIdIn(Set<Long> ids) {
        return entityManager.createQuery(
                        "SELECT r FROM Role r WHERE r.id IN :ids", Role.class)
                .setParameter("ids", ids)
                .getResultStream()
                .collect(Collectors.toSet());
    }

    @Override
    public void save(Role role) {
        entityManager.persist(role);
    }

    @Override
    public boolean existsByName(String name) {
        Long count = (Long) entityManager.createQuery(
                        "SELECT COUNT(r) FROM Role r WHERE r.role = :name")
                .setParameter("name", name)
                .getSingleResult();
        return count > 0;
    }
}