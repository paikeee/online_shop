package ru.nicetu.online_shop.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.nicetu.online_shop.models.Person;
import ru.nicetu.online_shop.models.PersonDetails;
import ru.nicetu.online_shop.repository.PersonRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonDetailsService implements UserDetailsService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return personRepository.findByEmail(email)
                .map(PersonDetails::build)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format("User with email %s not found", email)));
    }

    public void changePassword(String email, String password) {
        personRepository.changePassword(email, password);
    }

    public void changeName(String email, String name, String surname) {
        personRepository.changeName(email, name, surname);
    }

    public Person findByEmail(String email) throws UsernameNotFoundException {
        return personRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format("User with email %s not found", email)));
    }
}
