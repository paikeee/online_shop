package ru.nicetu.online_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.nicetu.online_shop.models.PersonRole;
import ru.nicetu.online_shop.models.Role;


import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(PersonRole name);
}

