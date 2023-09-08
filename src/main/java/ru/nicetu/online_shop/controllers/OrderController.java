package ru.nicetu.online_shop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.nicetu.online_shop.dto.request.OrderRequest;
import ru.nicetu.online_shop.dto.response.MessageResponse;
import ru.nicetu.online_shop.dto.response.OrderDTO;
import ru.nicetu.online_shop.models.Order;
import ru.nicetu.online_shop.services.OrderServiceImpl;

import javax.validation.Valid;


@RestController
@RequestMapping("/order")
@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
@AllArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;

    @PostMapping(path = "/new")
    public ResponseEntity<OrderDTO> newOrder(@RequestBody @Valid OrderRequest request) {
        return ResponseEntity.ok(orderService.newOrder(request));
    }

    @GetMapping(path = "/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable("orderId") int orderId) {
        Order order = orderService.getOrder(orderId);
        orderService.checkCustomer(order);
        return ResponseEntity.ok(new OrderDTO(
                order.getOrderId(),
                orderService.getTotalPrice(order.getOrderProducts()),
                order.getDate(),
                order.getOrderProducts()
        ));
    }

    @GetMapping(path = "/{orderId}/purchase")
    public ResponseEntity<?> makePurchase(@PathVariable("orderId") int orderId) {
        orderService.checkCustomer(orderService.getOrder(orderId));
        try {
            orderService.makePurchase(orderId);
        } catch (MailException mailException) {
            return new ResponseEntity<>("Unable to send email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(new MessageResponse("Purchase has been done. Check your email for further information"));
    }
}
