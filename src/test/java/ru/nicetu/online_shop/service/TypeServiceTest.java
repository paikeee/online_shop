package ru.nicetu.online_shop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nicetu.online_shop.dto.request.TypeRequest;
import ru.nicetu.online_shop.dto.response.ProductTypeResponse;
import ru.nicetu.online_shop.dto.response.TypeProductsDTO;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.models.Type;
import ru.nicetu.online_shop.repository.TypeRepository;
import ru.nicetu.online_shop.services.ProductServiceImpl;
import ru.nicetu.online_shop.services.TypeServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TypeServiceTest {

    @Mock
    private ProductServiceImpl productService;

    @Mock
    private TypeRepository typeRepository;

    @InjectMocks
    private TypeServiceImpl typeService;

    @Test
    public void testSave() {
        Type type = new Type();
        typeService.save(type);
        verify(typeRepository).save(type);
    }

    @Test
    public void testGetTypes() {
        List<Type> list = Collections.singletonList(new Type());
        when(typeRepository.findAll()).thenReturn(list);
        assertEquals(list, typeService.getTypes());
    }

    @Test
    public void testFindTypeById() {
        Type expected = new Type();
        expected.setTypeId(1);
        when(typeRepository.findByTypeId(1)).thenReturn(Optional.of(expected));
        Type result = typeService.findTypeById(expected.getTypeId());
        assertEquals(expected, result);
    }

    @Test
    public void testAddType() {
        Type expected = new Type("name", 0);
        TypeRequest request = new TypeRequest("name", 0, Collections.singletonList(1));
        Product product = new Product();
        product.setProductId(1);
        expected.setProductList(Collections.singletonList(product));

        when(productService.getProduct(1)).thenReturn(product);
        Type result = typeService.addType(request);
        assertEquals(expected, result);
        verify(typeRepository).save(result);
    }

    @Test
    public void testGetProductsByType() {
        Type type = new Type("name", 0);
        type.setTypeId(1);
        Product product = new Product();
        product.setProductId(1);
        type.setProductList(Collections.singletonList(product));

        List<Product> result = typeService.getProductsByType(type);
        assertEquals(Collections.singletonList(product), result);

        Type childType = new Type("name", 1);
        childType.setTypeId(2);
        Product product2 = new Product();
        product2.setProductId(2);
        childType.setProductList(Collections.singletonList(product2));
        List<Product> expected = new ArrayList<>();
        expected.add(product);
        expected.add(product2);

        when(typeRepository.findTypesByParentId(1)).thenReturn(Collections.singletonList(childType));
        result = typeService.getProductsByType(type);
        assertEquals(expected, result);
    }

    @Test
    public void testDelete() {
        Type type = new Type("name", 0);
        type.setTypeId(1);
        Type childType = new Type("name", 1);
        childType.setTypeId(2);
        List<Type> typeList = new ArrayList<>();
        typeList.add(type);
        typeList.add(childType);

        when(typeRepository.findByTypeId(1)).thenReturn(Optional.of(type));
        when(typeRepository.findAll()).thenReturn(typeList);
        typeService.delete(1);
        assertEquals(type.getParentId(), childType.getParentId());
        verify(typeRepository).save(childType);
        verify(typeRepository).deleteById(1);
    }

    @Test
    public void testAddProducts() {
        Type type = new Type("name", 0);
        type.setTypeId(1);
        Product product = new Product();
        product.setProductId(1);
        List<Product> expected = Collections.singletonList(product);

        when(typeRepository.findByTypeId(1)).thenReturn(Optional.of(type));
        when(productService.getProduct(1)).thenReturn(product);
        List<Product> result = typeService.addProducts(1, Collections.singletonList(1));
        assertEquals(expected, result);

        // Проверяем, что не добавляются дубликаты
        result = typeService.addProducts(1, Collections.singletonList(1));
        assertEquals(expected, result);
        verify(typeRepository, times(2)).save(any());
    }

    @Test
    public void testBuildTypeProducts() {
        Type type = new Type("name", 0);
        type.setTypeId(1);
        Product product = new Product("name", "desc", 100, 10, 10);
        product.setProductId(1);
        type.setProductList(Collections.singletonList(product));
        TypeProductsDTO expected = new TypeProductsDTO(
                Collections.singletonList(new ProductTypeResponse(product, 90, 5.0)),
                new ArrayList<>()
        );

        when(typeRepository.findByTypeId(1)).thenReturn(Optional.of(type));
        when(productService.getActualPrice(product)).thenReturn(90);
        when(productService.getRating(product)).thenReturn(5.0);
        assertEquals(expected, typeService.buildTypeProducts(1));
    }

    @Test
    public void testFindChildTypes() {
        Type type = new Type("name", 0);
        type.setTypeId(1);
        Type childType = new Type("name", 1);
        childType.setTypeId(2);
        List<Type> expected = new ArrayList<>();
        expected.add(childType);
        expected.add(type);

        when(typeRepository.findTypesByParentId(1)).thenReturn(Collections.singletonList(childType));
        assertEquals(expected, typeService.findChildTypes(type));
    }
}
