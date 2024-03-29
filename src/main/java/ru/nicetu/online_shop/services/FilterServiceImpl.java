package ru.nicetu.online_shop.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nicetu.online_shop.dto.request.FilterRequest;
import ru.nicetu.online_shop.dto.response.AttributeValueDTO;
import ru.nicetu.online_shop.dto.response.AttributesDTO;
import ru.nicetu.online_shop.dto.response.ProductTypeResponse;
import ru.nicetu.online_shop.dto.response.TypeProductsDTO;
import ru.nicetu.online_shop.models.AttributeValue;
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
    private final PersonDetailsService personDetailsService;
    private final ProductServiceImpl productService;

    private List<Product> filter(Type type, FilterRequest request) {
        List<Integer> valuesId = request.getValuesId();
        Set<Product> products = new HashSet<>();
        typeService.getProductsByType(type).forEach(it -> {
           if (new HashSet<>(it.getAttributeValues().stream()
                   .map(AttributeValue::getValueId)
                   .collect(Collectors.toList()))
                   .containsAll(valuesId)) {
               products.add(it);
           }
        });

        if (personDetailsService.isAuth()) {
            return products.stream().filter(it ->
                                    productService.getActualPrice(it) >= request.getMin()
                                    &&
                                    productService.getActualPrice(it) <= request.getMax()
                                    &&
                                    productService.getRating(it) >= request.getRating()
                    ).collect(Collectors.toList());
        } else {
            return products.stream().filter(it ->
                            it.getPrice() >= request.getMin()
                            &&
                            it.getPrice() <= request.getMax()
                            &&
                            productService.getRating(it) >= request.getRating()
                    ).collect(Collectors.toList());
        }
    }

    @Override
    public TypeProductsDTO buildFilteredType(int typeId, FilterRequest request) {
        Type type = typeService.findTypeById(typeId);
        return new TypeProductsDTO(
                filter(type, request).stream()
                        .map(it -> new ProductTypeResponse(
                                it,
                                productService.getActualPrice(it),
                                productService.getRating(it)
                        )).collect(Collectors.toList()),
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
