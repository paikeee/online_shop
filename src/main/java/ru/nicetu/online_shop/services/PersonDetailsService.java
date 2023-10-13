package ru.nicetu.online_shop.services;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.nicetu.online_shop.models.Person;
import ru.nicetu.online_shop.models.PersonDetails;
import ru.nicetu.online_shop.repository.PersonRepository;


@Service
@AllArgsConstructor
public class PersonDetailsService implements UserDetailsService {

    private PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return personRepository.findByEmail(email)
                .map(PersonDetails::build)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format("User with email %s not found", email)));
    }

    public Person findByEmail(String email) throws UsernameNotFoundException {
        return personRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format("User with email %s not found", email)));
    }

    public Person currentUser() {
        return findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public boolean isAuth() {
        return SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !(SecurityContextHolder.getContext().getAuthentication()
                        instanceof AnonymousAuthenticationToken);
    }

    public String username(Person person) {
        return person.getName() + " " + person.getSurname();
    }
}
