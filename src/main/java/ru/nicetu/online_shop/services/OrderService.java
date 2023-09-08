package ru.nicetu.online_shop.services;

import ru.nicetu.online_shop.models.Order;
import ru.nicetu.online_shop.models.OrderProduct;
import ru.nicetu.online_shop.models.Product;

import java.util.List;

public interface OrderService {

    Iterable<Order> getAllOrders();

    void create(Order order);

    void update(Order order);

    void addProduct(Order order, Product product, int quantity);

    int getTotalPrice(List<OrderProduct> orderProducts);

    public void saveOrderProduct(List<OrderProduct> orderProducts);

}
