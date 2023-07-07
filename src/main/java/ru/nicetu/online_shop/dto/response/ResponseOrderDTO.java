package ru.nicetu.online_shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class ResponseOrderDTO {

    private int orderId;
    private int totalPrice;
    private LocalDate date;

}
