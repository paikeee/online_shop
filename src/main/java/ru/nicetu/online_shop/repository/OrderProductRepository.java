package ru.nicetu.online_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nicetu.online_shop.models.OrderProduct;
import ru.nicetu.online_shop.models.OrderProductPK;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductPK> {

}
