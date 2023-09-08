package ru.nicetu.online_shop.services;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nicetu.online_shop.dto.request.LoginRequest;
import ru.nicetu.online_shop.dto.request.RegistrationRequest;
import ru.nicetu.online_shop.dto.response.JwtResponse;
import ru.nicetu.online_shop.dto.response.MessageResponse;
import ru.nicetu.online_shop.dto.response.PersonDTO;
import ru.nicetu.online_shop.models.Person;
import ru.nicetu.online_shop.models.PersonDetails;
import ru.nicetu.online_shop.models.PersonRole;
import ru.nicetu.online_shop.models.Role;
import ru.nicetu.online_shop.repository.PersonRepository;
import ru.nicetu.online_shop.repository.RoleRepository;
import ru.nicetu.online_shop.security.jwt.JwtUtils;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public ResponseEntity<?> register(RegistrationRequest request) {
        if (personRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
        }

        Person person = new Person(
                request.getEmail(),
                encoder.encode(request.getPassword()),
                request.getName(),
                request.getSurname()
        );

        Set<Role> roles = new HashSet<>();
        roles.add(
                roleRepository.findByName(PersonRole.ROLE_USER).
                orElseThrow(() -> new RuntimeException("Error: Role not found"))
        );
        person.setRole(roles);
        personRepository.save(person);
        return ResponseEntity.ok(new PersonDTO(person.getEmail(), person.getName(), person.getSurname()));
    }

    @Override
    public ResponseEntity<JwtResponse> login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String jwt = jwtUtils.generateJwtToken(authentication);

        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        List<String> roles = personDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity
                .ok(new JwtResponse(jwt, personDetails.getId(), personDetails.getUsername(), roles));
    }
}
