package ru.nicetu.online_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.nicetu.online_shop.models.Person;


import java.util.Optional;

@Repository
@Transactional
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByEmail(String email);

    boolean existsByEmail(String email);

//    @Modifying
//    @Query("UPDATE Person p set p.password = ?2 where p.email = ?1")
//    void changePassword(String email, String password);
//
//    @Modifying
//    @Query("UPDATE Person p set p.name = ?2, p.surname = ?3 where p.email = ?1")
//    void changeName(String email, String name, String surname);

}
