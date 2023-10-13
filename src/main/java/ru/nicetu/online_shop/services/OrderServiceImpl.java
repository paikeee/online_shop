package ru.nicetu.online_shop.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nicetu.online_shop.dto.request.OrderRequest;
import ru.nicetu.online_shop.dto.response.OrderDTO;
import ru.nicetu.online_shop.models.Order;
import ru.nicetu.online_shop.models.OrderProduct;
import ru.nicetu.online_shop.models.Person;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.repository.OrderRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductServiceImpl productService;
    private final PersonDetailsService personDetailsService;
    private final EmailServiceImpl emailService;

    @Override
    public Order getOrder(int id) {
        return orderRepository.findByOrderId(id)
                .orElseThrow(() -> new NoSuchElementException("Order with id" + id + "not found"));
    }

    @Override
    public void save(Order order) {
        this.orderRepository.save(order);
    }

    @Override
    public int getTotalPrice(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .mapToInt(this::getOrderProductPrice).sum();
    }

    @Override
    @Transactional
    public void deleteSelectedProducts(List<OrderProduct> orderProducts) {
        checkSelectedProducts(orderProducts);
        orderProducts.stream()
                .collect(Collectors.toMap(this::getProduct, OrderProduct::getQuantity))
                .forEach((product, quantity) -> {
                    product.setAmount(product.getAmount() - quantity);
                    productService.save(product);
                });
    }

    @Override
    public void checkSelectedProducts(List<OrderProduct> orderProducts) {
        orderProducts.stream()
                .collect(Collectors.toMap(this::getProduct, OrderProduct::getQuantity))
                .forEach((product, quantity) -> {
                    if (product.getAmount() < quantity) {
                        throw new NoSuchElementException("Bad quantity for product with id: " + product.getProductId());
                    }
                });
    }

    @Override
    @Transactional
    public OrderDTO newOrder(OrderRequest request) {
        Person person = personDetailsService.findByEmail(request.getEmail());
        Order order = new Order(person);
        List<OrderProduct> orderProducts = request.getOrderProducts().stream()
                .map(it -> new OrderProduct(
                        order,
                        productService.getProduct(it.getProductId()),
                        it.getQuantity()
                ))
                .collect(Collectors.toList());
        int totalPrice = getTotalPrice(orderProducts);
        deleteSelectedProducts(orderProducts);
        save(order);
        order.setOrderProducts(orderProducts);
        return new OrderDTO(
                order.getOrderId(),
                totalPrice,
                order.getDate(),
                order.getOrderProducts()
        );
    }

    @Override
    @Transactional
    public void makePurchase(int id) {
        Order order = getOrder(id);
        emailService.sendEmail(
                order.getPerson().getEmail(),
                "Order №" + id,
                buildPurchaseEmail(order)
        );
        order.setPaymentDone(true);
        save(order);
    }

    @Override
    public void checkCustomer(Order order) {
        if (!personDetailsService.currentUser().equals(order.getPerson())) {
            throw new RuntimeException("No access");
        }
    }

    @Override
    public Product getProduct(OrderProduct orderProduct) {
        return orderProduct.getPk().getProduct();
    }

    @Override
    public int getOrderProductPrice(OrderProduct orderProduct) {
        return productService.getActualPrice(getProduct(orderProduct)) * orderProduct.getQuantity();
    }

    @Override
    public String buildPurchaseEmail(Order order) {
        return "Hello, " + personDetailsService.username(order.getPerson()) + "!\n" +
                "Your order №" + order.getOrderId() + " has been successfully placed.\n" +
                "Your order:\n" +
                buildOrderMessage(order) +
                "Total: " + getTotalPrice(order.getOrderProducts()) + " RUB";
    }

    private String buildOrderMessage(Order order) {
        StringBuilder message = new StringBuilder();
        order.getOrderProducts().forEach(it -> message.append(orderProductToString(it)).append("\n"));
        return message.toString();
    }

    private String orderProductToString(OrderProduct orderProduct) {
        return getProduct(orderProduct).getName() +
                " * " + orderProduct.getQuantity() +
                " = " + getOrderProductPrice(orderProduct) + " RUB";
    }
}
