package ru.nicetu.online_shop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.nicetu.online_shop.models.Person;
import ru.nicetu.online_shop.models.PersonDetails;
import ru.nicetu.online_shop.models.PersonRole;
import ru.nicetu.online_shop.models.Role;
import ru.nicetu.online_shop.dto.request.LoginRequest;
import ru.nicetu.online_shop.dto.request.RegistrationRequest;
import ru.nicetu.online_shop.dto.response.JwtResponse;
import ru.nicetu.online_shop.dto.response.MessageResponse;
import ru.nicetu.online_shop.repository.PersonRepository;
import ru.nicetu.online_shop.repository.RoleRepository;
import ru.nicetu.online_shop.security.jwt.JwtUtils;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class AuthenticationController {

    @Autowired
    private final PersonRepository personRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final PasswordEncoder encoder;

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final JwtUtils jwtUtils;

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request) {
        if (personRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        Person person = new Person(
                request.getEmail(),
                encoder.encode(request.getPassword()),
                request.getName(),
                request.getSurname()
        );

        Set<String> requestRole = request.getRole();
        Set<Role> roles = new HashSet<>();

        if (requestRole == null) {
            Role role = roleRepository.findByName(PersonRole.USER).
                    orElseThrow(() -> new RuntimeException("Error: Role not found"));
            roles.add(role);
        } else {
            requestRole.forEach(role -> {
                if (role.equals("ADMIN")) {
                    Role adminRole = roleRepository.findByName(PersonRole.ADMIN).
                            orElseThrow(() -> new RuntimeException("Error: Role not found"));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(PersonRole.USER).
                            orElseThrow(() -> new RuntimeException("Error: Role not found"));
                    roles.add(userRole);
                }
            });
        }

        person.setRole(roles);
        personRepository.save(person);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String jwt = jwtUtils.generateJwtToken(authentication);

        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        List<String> roles = personDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity
                .ok(new JwtResponse(jwt, personDetails.getId(), personDetails.getUsername(), roles));
    }

}