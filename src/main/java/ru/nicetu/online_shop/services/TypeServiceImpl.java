package ru.nicetu.online_shop.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nicetu.online_shop.dto.request.TypeRequest;
import ru.nicetu.online_shop.dto.response.AttributeValueDTO;
import ru.nicetu.online_shop.dto.response.AttributesDTO;
import ru.nicetu.online_shop.dto.response.ProductTypeResponse;
import ru.nicetu.online_shop.dto.response.TypeProductsDTO;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.models.Type;
import ru.nicetu.online_shop.repository.TypeRepository;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TypeServiceImpl implements TypeService {

    private final TypeRepository typeRepository;
    private final ProductServiceImpl productService;

    @Override
    public void save(Type type) {
        typeRepository.save(type);
    }

    @Override
    public Iterable<Type> getTypes() {
        return typeRepository.findAll();
    }

    @Override
    public Type findTypeById(int id) {
        return typeRepository.findByTypeId(id)
                .orElseThrow(() -> new NoSuchElementException("Type with id " + id + "not found"));
    }

    @Override
    @Transactional
    public Type addType(TypeRequest request) {
        Type type = new Type(request.getTypeName(), request.getParentId());
        type.setProductList(
                request.getProductList().stream()
                        .map(productService::getProduct)
                        .collect(Collectors.toList()));
        save(type);
        return type;
    }

    @Override
    public List<Product> getProductsByType(Type type) {
        Set<Product> products = new HashSet<>(type.getProductList());
        typeRepository.findTypesByParentId(type.getTypeId())
                .forEach(it -> {
                    List<Product> childProduct = getProductsByType(it);
                    products.addAll(childProduct);
                });
        return new ArrayList<>(products);
    }

    @Override
    @Transactional
    public void delete(int id) {
        Type type = findTypeById(id);
        getTypes().forEach(it -> {
            if (it.getParentId() == type.getTypeId()) {
                it.setParentId(type.getParentId());
                typeRepository.save(it);
            }
        });
        typeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<Product> addProducts(int id, List<Integer> productsList) {
        Type type = findTypeById(id);
        Set<Product> productsSet = productsList.stream()
                .map(productService::getProduct)
                .collect(Collectors.toSet());
        productsSet.addAll(type.getProductList());
        List<Product> list = new ArrayList<>(productsSet);
        type.setProductList(list);
        save(type);
        return list;
    }

    @Override
    public TypeProductsDTO buildTypeProducts(int id) {
        Type type = findTypeById(id);
        return new TypeProductsDTO(
                getProductsByType(type).stream()
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
                                )).collect(Collectors.toList())
        );
    }

    @Override
    public List<Type> findChildTypes(Type type) {
        List<Type> typeList = new ArrayList<>();
        typeRepository.findTypesByParentId(type.getTypeId())
                .forEach(it -> typeList.addAll(findChildTypes(it)));
        typeList.add(type);
        return typeList;
    }
}
