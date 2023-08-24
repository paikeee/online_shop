package ru.nicetu.online_shop.services;

import ru.nicetu.online_shop.dto.request.ProductRequest;
import ru.nicetu.online_shop.models.Product;

import java.util.NoSuchElementException;

public interface ProductService {

    Iterable<Product> getAllProducts();

    Product getProduct(int id) throws NoSuchElementException;

    void save(Product product);

    void settings(Product product, ProductRequest settings);

    Product findById(int id);
}
