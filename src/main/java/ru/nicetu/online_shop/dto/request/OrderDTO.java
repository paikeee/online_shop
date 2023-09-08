package ru.nicetu.online_shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class OrderDTO {

    private String email;
    private List<OrderProductDTO> orderProducts;

}
