package ru.nicetu.online_shop.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nicetu.online_shop.dto.response.AttributeValueDTO;
import ru.nicetu.online_shop.dto.response.AttributesDTO;
import ru.nicetu.online_shop.models.Attribute;
import ru.nicetu.online_shop.models.AttributeValue;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.models.Type;
import ru.nicetu.online_shop.repository.AttributeRepository;
import ru.nicetu.online_shop.repository.AttributeValueRepository;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository attributeRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final TypeServiceImpl typeService;
    private final ProductServiceImpl productService;

    @Override
    public Attribute get(int id) {
        return attributeRepository.findAttributeByAttributeId(id)
                .orElseThrow(() -> new NoSuchElementException("Attribute with id " + id + " not found"));
    }

    @Override
    public AttributeValue getValue(int id) {
        return attributeValueRepository.findAttributeValueByValueId(id)
                .orElseThrow(() -> new NoSuchElementException("Attribute value with id " + id + " not found"));
    }

    @Override
    @Transactional
    public void saveAttributes(int id, List<String> names) {
        List<Type> types = typeService.findChildTypes(typeService.findTypeById(id));
        names.forEach(it -> {
            Attribute attribute = new Attribute(it);
            attributeRepository.save(attribute);
            attribute.setTypes(types);
        });
    }

    @Override
    @Transactional
    public void saveProductAttributes(int id, Map<Integer, String> values) {
        Map<Integer, List<AttributeValue>> attributeListMap = new HashMap<>();
        values.keySet().forEach(it -> attributeListMap.put(it, get(it).getAttributeValues()));
        values.forEach((key, value) -> {
            AttributeValue attributeValue = new AttributeValue(value);
            attributeValueRepository.save(attributeValue);
            attributeListMap.get(key).add(attributeValue);
            List<Product> productList = attributeValue.getProducts();
            productList.add(productService.getProduct(id));
            attributeValue.setProducts(productList);
       });
    }


    @Override
    public List<Attribute> getAttributes(List<Type> types) {
        Set<Attribute> attributes = new HashSet<>();
        types.forEach(it -> attributes.addAll(it.getAttributes()));
        return new ArrayList<>(attributes);
    }

    @Override
    public List<AttributesDTO> buildAttributesDTO(List<Type> types) {
        List<Attribute> attributes = getAttributes(types);
        return attributes.stream().map(it -> {
                List<AttributeValueDTO> values = it.getAttributeValues().stream()
                        .map(value -> new AttributeValueDTO(value.getValueId(), value.getValue()))
                        .collect(Collectors.toList());
                return new AttributesDTO(it.getAttributeId(), it.getName(), values);
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteValue(int id) {
        attributeValueRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAttribute(int id) {
        get(id).getAttributeValues().forEach(it -> deleteValue(it.getValueId()));
        attributeRepository.deleteById(id);
    }
}
