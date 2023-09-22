package ru.nicetu.online_shop.services;

import org.springframework.web.multipart.MultipartFile;
import ru.nicetu.online_shop.dto.request.ProductRequest;
import ru.nicetu.online_shop.models.Product;

import java.util.List;
import java.util.NoSuchElementException;

public interface ProductService {


    Product getProduct(int id) throws NoSuchElementException;

    Product save(ProductRequest request, List<MultipartFile> files);

    void save(Product product);

    void settings(Product product, ProductRequest settings);

    Product findById(int id);
}
