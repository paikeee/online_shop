package ru.nicetu.online_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.nicetu.online_shop.models.Type;

import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<Type, Integer> {

    Optional<Type> findByTypeId(int id);

    Iterable<Type> findTypesByParentId(int parentId);

}
