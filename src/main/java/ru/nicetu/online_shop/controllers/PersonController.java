package ru.nicetu.online_shop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nicetu.online_shop.repository.PersonRepository;
import ru.nicetu.online_shop.services.PersonDetailsService;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class PersonController {

    private final PersonDetailsService personDetailsService;

    @GetMapping(path = "/info")
    public String userInfo() {
        return personDetailsService.findByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()).toString();
    }
}
