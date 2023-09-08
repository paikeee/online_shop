package ru.nicetu.online_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nicetu.online_shop.models.Attribute;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface AttributeRepository extends JpaRepository<Attribute, Integer> {

    Optional<Attribute> findAttributeByAttributeId(int attributeId);
}
