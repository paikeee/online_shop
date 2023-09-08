package ru.nicetu.online_shop.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nicetu.online_shop.dto.request.ProductRequest;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.repository.ProductRepository;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    @Override
    public Product getProduct(int id) throws NoSuchElementException {
        return productRepository.findByProductId(id)
                .orElseThrow(() -> new NoSuchElementException("Product with id " + id + "not found"));
    }

    @Override
    @Transactional
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void settings(Product product, ProductRequest settings) {
        product.setName(settings.getName());
        product.setDescription(settings.getDescription());
        product.setAmount(settings.getAmount());
        product.setDiscount(settings.getDiscount());
        product.setPrice(settings.getPrice());
        save(product);
    }

    @Override
    public Product findById(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product with id " + id + "not found"));
    }
}
