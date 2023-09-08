package ru.nicetu.online_shop.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nicetu.online_shop.dto.request.FilterRequest;
import ru.nicetu.online_shop.dto.response.AttributeValueDTO;
import ru.nicetu.online_shop.dto.response.AttributesDTO;
import ru.nicetu.online_shop.dto.response.ProductTypeResponse;
import ru.nicetu.online_shop.dto.response.TypeProductsDTO;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.models.Type;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FilterServiceImpl implements FilterService {

    private final TypeServiceImpl typeService;
    private final AttributeServiceImpl attributeService;
    private final PersonDetailsService personDetailsService;

    private List<Product> filter(Type type, FilterRequest request) {
        Set<Product> products = new HashSet<>();
        List<Integer> valuesId = request.getValuesId();

        if (valuesId.isEmpty()) {
            products.addAll(typeService.getProductsByType(type));
        } else {
            valuesId.stream()
                    .map(attributeService::getValue)
                    .forEach(it -> products.addAll(it.getProducts()));
        }

        if (personDetailsService.isAuth()) {
            return products.stream().filter(it ->
                            it.getActualPrice() >= request.getMin()
                            &&
                            it.getActualPrice() <= request.getMax()
                            &&
                            it.getRating() >= request.getRating()
                    ).collect(Collectors.toList());
        } else {
            return products.stream().filter(it ->
                    it.getActualPrice() >= request.getMin()
                            &&
                            it.getActualPrice() <= request.getMax()
                            &&
                            it.getRating() >= request.getRating()
                    ).collect(Collectors.toList());
        }
    }

    @Override
    public TypeProductsDTO buildFilteredType(int typeId, FilterRequest request) {
        Type type = typeService.findTypeById(typeId);
        return new TypeProductsDTO(
                filter(type, request).stream()
                        .map(ProductTypeResponse::new)
                        .collect(Collectors.toList()),
                type.getAttributes().stream()
                        .map(it -> new AttributesDTO(
                                it.getAttributeId(),
                                it.getName(),
                                it.getAttributeValues().stream()
                                        .map(value -> new AttributeValueDTO(
                                                value.getValueId(),
                                                value.getValue()
                                        )).collect(Collectors.toList())
                        )).collect(Collectors.toList()));
    }
}
