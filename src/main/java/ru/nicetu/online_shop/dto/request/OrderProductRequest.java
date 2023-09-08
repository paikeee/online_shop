package ru.nicetu.online_shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class OrderProductRequest {

    @NotNull
    private int productId;

    @NotNull
    @Min(value = 1)
    private int quantity;

}
