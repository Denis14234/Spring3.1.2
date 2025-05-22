package ru.kata.spring.boot_security.demo.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.dao.UserDaoImp;
import ru.kata.spring.boot_security.demo.model.User;

import java.security.Principal;


@Controller
public class HelloController {
    private final UserDaoImp ud;

    public HelloController(UserDaoImp ud) {
        this.ud = ud;
    }

    @GetMapping(value = "/user")
    public String printWelcome(ModelMap model, Principal princ) {
        User us = ud.findByName(princ.getName());
        model.addAttribute("messages", us);
        return "user";
    }
}