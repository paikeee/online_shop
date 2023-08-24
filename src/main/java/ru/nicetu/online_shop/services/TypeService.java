package ru.nicetu.online_shop.services;

import ru.nicetu.online_shop.dto.request.TypeRequest;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.models.Type;

import java.util.List;

public interface TypeService {

    void save(Type type);

    Iterable<Type> getTypes();

    Type findTypeById(int id);

    void addType(Type type, TypeRequest request);

    List<Product> getProductsByType(int id);

    void delete(int id);

    void addProducts(int id, List<Integer> productsList);

}
