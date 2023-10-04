package ru.nicetu.online_shop.services;

import ru.nicetu.online_shop.dto.response.AttributesDTO;
import ru.nicetu.online_shop.models.Attribute;
import ru.nicetu.online_shop.models.AttributeValue;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.models.Type;

import java.util.List;
import java.util.Map;

public interface AttributeService {

    Attribute get(int id);

    AttributeValue getValue(int id);

    void saveAttributes(int id, List<String> names);

    Product saveProductAttributes(int id, Map<Integer, String> values);

    List<Attribute> getAttributes(List<Type> types);

    List<AttributesDTO> buildAttributesDTO(List<Type> types);

    void deleteValue(int id);

    void deleteAttribute(int id);
}
