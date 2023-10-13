package ru.nicetu.online_shop.dto.request;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ProductRequest {

    @Size(min = 1, max = 255, message = "Name size is 1 to 255")
    private String name;

    @NotEmpty
    private String description;

    @NotNull
    private int price;

    @NotNull
    private int amount;

    @NotNull
    private int discount;

}
