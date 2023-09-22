package ru.nicetu.online_shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.nicetu.online_shop.models.OrderProduct;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderDTO {

    private int orderId;
    private int totalPrice;
    private LocalDate date;
    private List<OrderProduct> orderProducts;

}
