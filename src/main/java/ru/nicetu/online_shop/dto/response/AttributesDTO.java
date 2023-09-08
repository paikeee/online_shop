package ru.nicetu.online_shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class AttributesDTO {

    private int attributeId;
    private String name;
    private List<AttributeValueDTO> attributeValues;

}
