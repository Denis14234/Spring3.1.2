package ru.kata.spring.boot_security.demo.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;


import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public User findByName(String username) {
        return em.createQuery("select u FROM User u JOIn fETCH u.roles WHERe u.username = :id", User.class)
                .setParameter("id", username)
                .getResultList().stream().findAny().orElse(null);
    }

    @Override
    public  void delete(Long id) {
        User us = em.find(User.class, id);
        em.remove(us);
    }

    @Override
    public void update(User us) {
        em.merge(us);
    }

    @Override
    public boolean add(User user) {
        em.persist(user);
        return true;
    }

    @Override
    public List<User> listUsers() {
        return em.createQuery("select s from User s", User.class).getResultList();
    }

    @Override
    public User findById(Long id) {
        return em.find(User.class, id);
    }
}