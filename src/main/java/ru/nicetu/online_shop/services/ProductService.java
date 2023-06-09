package ru.nicetu.online_shop.services;

import ru.nicetu.online_shop.models.Product;

import java.util.NoSuchElementException;

public interface ProductService {

    Iterable<Product> getAllProducts();

    Product getProduct(int id) throws NoSuchElementException;

    void save(Product product);
}
