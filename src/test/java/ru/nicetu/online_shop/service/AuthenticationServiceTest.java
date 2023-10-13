package ru.nicetu.online_shop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.nicetu.online_shop.dto.request.LoginRequest;
import ru.nicetu.online_shop.dto.request.RegistrationRequest;
import ru.nicetu.online_shop.models.Person;
import ru.nicetu.online_shop.models.PersonDetails;
import ru.nicetu.online_shop.models.PersonRole;
import ru.nicetu.online_shop.models.Role;
import ru.nicetu.online_shop.repository.PersonRepository;
import ru.nicetu.online_shop.repository.RoleRepository;
import ru.nicetu.online_shop.security.jwt.JwtUtils;
import ru.nicetu.online_shop.services.AuthenticationServiceImpl;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;


    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    public void testRegister() {
        RegistrationRequest request = new RegistrationRequest(
                "email@email.ru",
                "password1",
                "name",
                "surname"
        );
        Person person = new Person(
          request.getEmail(),
          encoder.encode(request.getPassword()),
          request.getName(),
          request.getSurname()
        );
        Role role = new Role();
        role.setName(PersonRole.ROLE_USER);
        person.setRole(new HashSet<>(Collections.singletonList(role)));
        when(roleRepository.findByName(PersonRole.ROLE_USER)).thenReturn(Optional.of(role));
        authenticationService.register(request);
        verify(personRepository).save(person);
    }

    @Test
    public void testLogin() {
        LoginRequest request = new LoginRequest("email@email.ru", "password1");

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<>();
        updatedAuthorities.add(authority);

        PersonDetails personDetails = new PersonDetails(
                0,
                request.getEmail(),
                request.getPassword(),
                updatedAuthorities
        );

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                personDetails, request.getPassword()
        );

        when(authenticationManager.authenticate(any())).thenReturn(token);
        authenticationService.login(request);
        verify(jwtUtils).generateJwtToken(token);
    }
}
