package ru.nicetu.online_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nicetu.online_shop.models.AttributeValue;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface AttributeValueRepository extends JpaRepository<AttributeValue, Integer> {

    Optional<AttributeValue> findAttributeValueByValueId(int id);

}
