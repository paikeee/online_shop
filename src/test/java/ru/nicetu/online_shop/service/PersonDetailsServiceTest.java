package ru.nicetu.online_shop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.nicetu.online_shop.models.Person;
import ru.nicetu.online_shop.models.PersonDetails;
import ru.nicetu.online_shop.repository.PersonRepository;
import ru.nicetu.online_shop.services.PersonDetailsService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonDetailsServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private Authentication auth;

    @InjectMocks
    private PersonDetailsService personDetailsService;


    @Test
    public void testLoadUserByUsername() {
        Person person = new Person(
                "email@email.ru",
                "password1",
                "name",
                "surname"
        );
        PersonDetails expected = PersonDetails.build(person);
        when(personRepository.findByEmail(person.getEmail())).thenReturn(Optional.of(person));
        PersonDetails result = (PersonDetails) personDetailsService.loadUserByUsername(person.getEmail());
        assertEquals(expected, result);
        assertThrows(UsernameNotFoundException.class, () -> personDetailsService.loadUserByUsername("1"));
    }

    @Test
    public void testFindByEmail() {
        Person expected = new Person(
                "email@email.ru",
                "password1",
                "name",
                "surname"
        );
        when(personRepository.findByEmail(expected.getEmail())).thenReturn(Optional.of(expected));
        Person result = personDetailsService.findByEmail(expected.getEmail());
        assertEquals(expected, result);
        assertThrows(UsernameNotFoundException.class, () -> personDetailsService.findByEmail("1"));
    }

    @Test
    public void testUsername() {
        Person person = new Person(
                "email@email.ru",
                "password1",
                "name",
                "surname"
        );
        assertEquals("name surname", personDetailsService.username(person));
    }

    @Test
    public void testCurrentUser() {
        Person expected = new Person(
                "email@email.ru",
                "password1",
                "name",
                "surname"
        );
        when(auth.getName()).thenReturn("email@email.ru");
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(personRepository.findByEmail(expected.getEmail())).thenReturn(Optional.of(expected));
        Person result = personDetailsService.currentUser();
        assertEquals(expected, result);
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testIsAuth() {
        assertFalse(personDetailsService.isAuth());

        SecurityContextHolder.getContext().setAuthentication(auth);
        assertFalse(personDetailsService.isAuth());

        when(auth.isAuthenticated()).thenReturn(true);
        assertTrue(personDetailsService.isAuth());

        SecurityContextHolder.clearContext();
    }

}
