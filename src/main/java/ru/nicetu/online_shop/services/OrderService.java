package ru.nicetu.online_shop.services;

import ru.nicetu.online_shop.dto.request.OrderRequest;
import ru.nicetu.online_shop.dto.response.OrderDTO;
import ru.nicetu.online_shop.models.Order;
import ru.nicetu.online_shop.models.OrderProduct;
import ru.nicetu.online_shop.models.Product;

import java.util.List;

public interface OrderService {

    Order getOrder(int id);

    void save(Order order);

    int getTotalPrice(List<OrderProduct> orderProducts);

    void deleteSelectedProducts(List<OrderProduct> orderProducts);

    void checkSelectedProducts(List<OrderProduct> orderProducts);

    OrderDTO newOrder(OrderRequest request);

    void makePurchase(int id);

    void checkCustomer(Order order);

    Product getProduct(OrderProduct orderProduct);

    int getOrderProductPrice(OrderProduct orderProduct);

    String buildPurchaseEmail(Order order);

}
