package ru.nicetu.online_shop.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nicetu.online_shop.dto.request.TypeRequest;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.models.Type;
import ru.nicetu.online_shop.repository.TypeRepository;

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
    public void addType(Type type, TypeRequest request) {
        type.setProductList(
                request.getProductList().stream()
                        .map(productService::findById)
                        .collect(Collectors.toList()));
        save(type);
    }

    public List<Product> getProductsByType(int id) {
        Set<Product> products = new HashSet<>(findTypeById(id).getProductList());
        typeRepository.findTypesByParentId(id)
                .forEach(it -> products.addAll(getProductsByType(it.getTypeId())));
        return new ArrayList<>(products);
    }

    @Override
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
    public void addProducts(int id, List<Integer> productsList) {
        Type type = findTypeById(id);
        Set<Product> productsSet = productsList.stream()
                .map(productService::findById)
                .collect(Collectors.toSet());
        productsSet.addAll(type.getProductList());
        type.setProductList(new ArrayList<>(productsSet));
        save(type);
    }
}
