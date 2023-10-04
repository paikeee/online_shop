package ru.nicetu.online_shop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nicetu.online_shop.dto.request.FilterRequest;
import ru.nicetu.online_shop.dto.response.AttributeValueDTO;
import ru.nicetu.online_shop.dto.response.AttributesDTO;
import ru.nicetu.online_shop.dto.response.ProductTypeResponse;
import ru.nicetu.online_shop.dto.response.TypeProductsDTO;
import ru.nicetu.online_shop.models.Attribute;
import ru.nicetu.online_shop.models.AttributeValue;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.models.Type;
import ru.nicetu.online_shop.services.FilterServiceImpl;
import ru.nicetu.online_shop.services.PersonDetailsService;
import ru.nicetu.online_shop.services.ProductServiceImpl;
import ru.nicetu.online_shop.services.TypeServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilterServiceTest {

    @Mock
    private TypeServiceImpl typeService;

    @Mock
    private ProductServiceImpl productService;

    @Mock
    private PersonDetailsService personDetailsService;

    @InjectMocks
    private FilterServiceImpl filterService;

    @Test
    public void testBuildFilteredType() {
        Attribute attribute = new Attribute("color");
        AttributeValue attributeValue = new AttributeValue("white");
        attributeValue.setValueId(1);
        attribute.setAttributeValues(new ArrayList<>(Collections.singletonList(attributeValue)));

        AttributeValueDTO attributeValueDTO = new AttributeValueDTO(1, "white");
        AttributesDTO attributesDTO = new AttributesDTO(
                0, "color", Collections.singletonList(attributeValueDTO)
        );
        List<AttributesDTO> attributesDTOList = new ArrayList<>();
        attributesDTOList.add(attributesDTO);

        Product product1 = new Product("name1", "", 10000, 10, 10);
        product1.setProductId(1);
        product1.setAttributeValues(Collections.singletonList(attributeValue));
        Product product2 = new Product("name2", "", 10000, 10, 0);
        product2.setProductId(2);
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        ProductTypeResponse productTypeResponse1 = new ProductTypeResponse(product1, 9000, 0.0);
        ProductTypeResponse productTypeResponse2 = new ProductTypeResponse(product2, 10000, 5.0);
        List<ProductTypeResponse> productTypeResponseList = new ArrayList<>();
        productTypeResponseList.add(productTypeResponse1);
        productTypeResponseList.add(productTypeResponse2);

        Type type = new Type();
        type.setAttributes(Collections.singletonList(attribute));
        type.setProductList(products);

        TypeProductsDTO expected1 = new TypeProductsDTO(
                productTypeResponseList, attributesDTOList
        );
        TypeProductsDTO expected2 = new TypeProductsDTO(
                Collections.singletonList(productTypeResponse2), attributesDTOList
        );
        TypeProductsDTO expected3 = new TypeProductsDTO(
                new ArrayList<>(), attributesDTOList
        );
        TypeProductsDTO expected4 = new TypeProductsDTO(
                Collections.singletonList(productTypeResponse1), attributesDTOList
        );

        FilterRequest request1 = new FilterRequest(
                new ArrayList<>(), 0, 20000, 0.0
        );
        FilterRequest request2 = new FilterRequest(
                new ArrayList<>(), 0, 20000, 4.0
        );
        FilterRequest request3 = new FilterRequest(
                new ArrayList<>(), 0, 9500, 0.0
        );
        FilterRequest request4 = new FilterRequest(
                new ArrayList<>(), 0, 9500, 0.0
        );
        FilterRequest request5 = new FilterRequest(
                Collections.singletonList(1), 0, 20000, 0.0
        );

        when(typeService.findTypeById(0)).thenReturn(type);
        when(typeService.getProductsByType(type)).thenReturn(type.getProductList());
        when(productService.getActualPrice(product1)).thenReturn(9000);
        when(productService.getActualPrice(product2)).thenReturn(10000);
        when(productService.getRating(product1)).thenReturn(0.0);
        when(productService.getRating(product2)).thenReturn(5.0);


        TypeProductsDTO result1 = filterService.buildFilteredType(0, request1);
        result1.setProducts(
                result1.getProducts().stream()
                        .sorted(Comparator.comparing(ProductTypeResponse::getProductId))
                        .collect(Collectors.toList()));
        TypeProductsDTO result2 = filterService.buildFilteredType(0, request2);
        TypeProductsDTO result3 = filterService.buildFilteredType(0, request3);

        when(personDetailsService.isAuth()).thenReturn(true);
        TypeProductsDTO result4 = filterService.buildFilteredType(0, request4);
        TypeProductsDTO result5 = filterService.buildFilteredType(0, request5);

        assertEquals(expected1, result1);
        assertEquals(expected2, result2);
        assertEquals(expected3, result3);
        assertEquals(expected4, result4);
        assertEquals(expected4, result5);
    }
}
