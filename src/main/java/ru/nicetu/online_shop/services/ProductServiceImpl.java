package ru.nicetu.online_shop.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.repository.ProductRepository;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(int id) throws NoSuchElementException {
        return productRepository.findByProductId(id)
                .orElseThrow(() -> new NoSuchElementException("Product with id " + id + "not found"));
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }
}
