package ru.nicetu.online_shop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nicetu.online_shop.dto.request.OrderDTO;
import ru.nicetu.online_shop.dto.response.ResponseOrderDTO;
import ru.nicetu.online_shop.models.Order;
import ru.nicetu.online_shop.models.OrderProduct;
import ru.nicetu.online_shop.models.Person;
import ru.nicetu.online_shop.services.OrderServiceImpl;
import ru.nicetu.online_shop.services.PersonDetailsService;
import ru.nicetu.online_shop.services.ProductServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;
    private final PersonDetailsService personDetailsService;
    private final ProductServiceImpl productService;

    @PostMapping(path = "/new")
    public ResponseEntity<?> newOrder(@RequestBody OrderDTO orderDTO) {

        Person person = personDetailsService.findByEmail(orderDTO.getEmail());
        Order order = new Order(person);
        List<OrderProduct> orderProducts = orderDTO.getOrderProducts().stream()
                .map(it -> new OrderProduct(
                        order,
                        productService.getProduct(it.getProductId()),
                        it.getQuantity()
                ))
                .collect(Collectors.toList());
        int totalPrice = orderService.getTotalPrice(orderProducts);

//        orderService.saveOrderProduct(orderProducts);
        order.setOrderProducts(orderProducts);
        orderService.create(order);
        return ResponseEntity.ok(new ResponseOrderDTO(
                order.getOrderId(),
                totalPrice,
                order.getDate()
        ));
    }
}
