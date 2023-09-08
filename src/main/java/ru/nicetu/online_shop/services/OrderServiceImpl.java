package ru.nicetu.online_shop.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nicetu.online_shop.models.Order;
import ru.nicetu.online_shop.models.OrderProduct;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.repository.OrderProductRepository;
import ru.nicetu.online_shop.repository.OrderRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductServiceImpl productService;
    private final OrderProductRepository orderProductRepository;

    @Override
    public Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void create(Order order) {
        this.orderRepository.save(order);
    }

    @Override
    public void update(Order order) {
        this.orderRepository.save(order);
    }

    @Override
    public void addProduct(Order order, Product product, int quantity) {
        List<OrderProduct> orderProducts = order.getOrderProducts();
        orderProducts.add(new OrderProduct(order, product, quantity));
        order.setOrderProducts(orderProducts);
        update(order);
    }

    @Override
    public int getTotalPrice(List<OrderProduct> orderProducts) {
        int sum = orderProducts.stream()
                .mapToInt(OrderProduct::getTotalPrice)
                .sum();
        orderProducts.stream()
                .collect(Collectors.toMap(OrderProduct::getProduct, OrderProduct::getQuantity))
                .forEach((product, quantity) -> {
                    product.setAmount(product.getAmount() - quantity);
                    productService.save(product);
                });
        return sum;
    }

    @Override
    public void saveOrderProduct(List<OrderProduct> orderProducts) {
        orderProductRepository.saveAll(orderProducts);
    }
}
