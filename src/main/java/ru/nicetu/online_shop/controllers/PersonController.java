package ru.nicetu.online_shop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nicetu.online_shop.services.PersonDetailsService;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class PersonController {

    private final PersonDetailsService personDetailsService;

    @GetMapping(path = "/info")
    public String userInfo() {
        return personDetailsService.currentUser().toString();
    }
}
