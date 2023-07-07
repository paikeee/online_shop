package ru.nicetu.online_shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderProductDTO {

    private int productId;
    private int quantity;

}
