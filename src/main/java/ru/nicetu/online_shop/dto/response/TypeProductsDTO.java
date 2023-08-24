package ru.nicetu.online_shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.nicetu.online_shop.models.Product;

import java.util.List;

@AllArgsConstructor
@Getter
public class TypeProductsDTO {

    private List<ProductTypeResponse> products;

}
