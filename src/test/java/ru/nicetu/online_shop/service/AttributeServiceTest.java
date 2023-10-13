package ru.nicetu.online_shop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nicetu.online_shop.dto.response.AttributesDTO;
import ru.nicetu.online_shop.models.Attribute;
import ru.nicetu.online_shop.models.AttributeValue;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.models.Type;
import ru.nicetu.online_shop.repository.AttributeRepository;
import ru.nicetu.online_shop.repository.AttributeValueRepository;
import ru.nicetu.online_shop.services.AttributeServiceImpl;
import ru.nicetu.online_shop.services.ProductServiceImpl;
import ru.nicetu.online_shop.services.TypeServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AttributeServiceTest {

    @Mock
    private AttributeRepository attributeRepository;

    @Mock
    private AttributeValueRepository attributeValueRepository;

    @Mock
    private TypeServiceImpl typeService;

    @Mock
    private ProductServiceImpl productService;

    @InjectMocks
    private AttributeServiceImpl attributeService;

    @Test
    public void testGet(){
        Attribute attribute = new Attribute();
        when(attributeRepository.findAttributeByAttributeId(0)).thenReturn(Optional.of(attribute));
        assertEquals(attribute, attributeService.get(0));
    }

    @Test
    public void testGetValue() {
        AttributeValue attributeValue = new AttributeValue();
        when(attributeValueRepository.findAttributeValueByValueId(0)).thenReturn(Optional.of(attributeValue));
        assertEquals(attributeValue, attributeService.getValue(0));
    }

    @Test
    public void testSaveAttributes() {
        Type type = new Type();
        type.setTypeId(1);
        List<Type> types = Collections.singletonList(type);
        Attribute attribute = new Attribute("name");
        attribute.setTypes(types);

        when(typeService.findTypeById(1)).thenReturn(type);
        when(typeService.findChildTypes(type)).thenReturn(Collections.singletonList(type));
        attributeService.saveAttributes(1, Collections.singletonList("name"));
        verify(attributeRepository).save(attribute);
    }

    @Test
    public void testSaveProductAttributes() {
        Attribute attribute1 = new Attribute("color");
        attribute1.setAttributeId(1);
        AttributeValue value1 = new AttributeValue("white");
        value1.setValueId(1);
        attribute1.setAttributeValues(Collections.singletonList(value1));

        Product product = new Product();
        product.setProductId(1);
        product.setAttributeValues(Collections.singletonList(value1));

        Attribute attribute2 = new Attribute("model");
        attribute2.setAttributeId(2);
        AttributeValue value2 = new AttributeValue("iphone");

        Map<Integer, String> test = new HashMap<>();
        test.put(2, "iphone");
        List<AttributeValue> expected = new ArrayList<>();
        expected.add(value1);
        expected.add(value2);

        when(attributeRepository.findAttributeByAttributeId(2)).thenReturn(Optional.of(attribute2));
        when(productService.getProduct(1)).thenReturn(product);
        attributeService.saveProductAttributes(1, test);
        assertEquals(expected, product.getAttributeValues());
        verify(attributeValueRepository).save(value2);
    }

    @Test
    public void testGetAttributes() {
        Type type1 = new Type();
        type1.setTypeId(1);
        Type type2 = new Type();
        type2.setTypeId(2);
        Attribute attribute = new Attribute();
        List<Attribute> list = Collections.singletonList(attribute);
        type1.setAttributes(list);
        type2.setAttributes(list);
        List<Type> typeList = new ArrayList<>();
        typeList.add(type1);
        typeList.add(type2);

        assertEquals(list, attributeService.getAttributes(typeList));
    }

    @Test
    public void testBuildAttributesDTO() {
        Type type1 = new Type();
        type1.setTypeId(1);
        Type type2 = new Type();
        type2.setTypeId(2);
        Attribute attribute = new Attribute("name");
        List<Attribute> list = Collections.singletonList(attribute);
        type1.setAttributes(list);
        type2.setAttributes(list);
        List<Type> typeList = new ArrayList<>();
        typeList.add(type1);
        typeList.add(type2);

        List<AttributesDTO> expected = Collections.singletonList(
                new AttributesDTO(0, "name", new ArrayList<>())
        );
        assertEquals(expected, attributeService.buildAttributesDTO(typeList));
    }

    @Test
    public void testDeleteValue() {
        attributeService.deleteValue(0);
        verify(attributeValueRepository).deleteById(0);
    }

    @Test
    public void testDeleteAttribute() {
        Attribute attribute = new Attribute();
        AttributeValue value = new AttributeValue();
        attribute.setAttributeValues(Collections.singletonList(value));

        when(attributeRepository.findAttributeByAttributeId(0)).thenReturn(Optional.of(attribute));
        attributeService.deleteAttribute(0);
        verify(attributeValueRepository).deleteById(0);
        verify(attributeRepository).deleteById(0);
    }
}
