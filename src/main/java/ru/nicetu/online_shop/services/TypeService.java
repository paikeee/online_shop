package ru.nicetu.online_shop.services;

import ru.nicetu.online_shop.dto.request.TypeRequest;
import ru.nicetu.online_shop.dto.response.TypeProductsDTO;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.models.Type;

import java.util.List;

public interface TypeService {

    void save(Type type);

    Iterable<Type> getTypes();

    Type findTypeById(int id);

    Type addType(TypeRequest request);

    List<Product> getProductsByType(Type type);

    void delete(int id);

    List<Product> addProducts(int id, List<Integer> productsList);

    TypeProductsDTO buildTypeProducts(int id);

    List<Type> findChildTypes(Type type);

}
