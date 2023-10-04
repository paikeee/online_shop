package ru.nicetu.online_shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class AttributeValueDTO {

    private int valueId;
    private String value;

}
