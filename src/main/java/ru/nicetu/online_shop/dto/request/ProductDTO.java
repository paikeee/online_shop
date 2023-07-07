package ru.nicetu.online_shop.dto.request;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ProductDTO {
    private String name;
    private int price;
    private int amount;
    private int discount;
    private String pictureUrl;
}
