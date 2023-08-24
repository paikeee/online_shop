package ru.nicetu.online_shop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nicetu.online_shop.dto.request.LoginRequest;
import ru.nicetu.online_shop.dto.request.RegistrationRequest;
import ru.nicetu.online_shop.services.AuthenticationServiceImpl;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request) {
        return authenticationService.register(request);
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        return authenticationService.login(request);
    }

}