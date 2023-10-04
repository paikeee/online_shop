package ru.nicetu.online_shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class TypeProductsDTO {

    private List<ProductTypeResponse> products;
    private List<AttributesDTO> attributes;

}
