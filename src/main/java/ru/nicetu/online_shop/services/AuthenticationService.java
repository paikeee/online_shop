package ru.nicetu.online_shop.services;

import org.springframework.http.ResponseEntity;
import ru.nicetu.online_shop.dto.request.LoginRequest;
import ru.nicetu.online_shop.dto.request.RegistrationRequest;
import ru.nicetu.online_shop.dto.response.JwtResponse;

public interface AuthenticationService {

    ResponseEntity<?> register (RegistrationRequest request);

    ResponseEntity<JwtResponse> login(LoginRequest request);

}
