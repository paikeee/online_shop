package ru.nicetu.online_shop.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.nicetu.online_shop.dto.request.ProductRequest;
import ru.nicetu.online_shop.models.Comment;
import ru.nicetu.online_shop.models.Picture;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.repository.ProductRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final PictureServiceImpl pictureService;


    @Override
    public Product getProduct(int id) throws NoSuchElementException {
        return productRepository.findByProductId(id)
                .orElseThrow(() -> new NoSuchElementException("Product with id " + id + "not found"));
    }

    @Override
    @Transactional
    public Product save(ProductRequest request, List<MultipartFile> files) {
        List<Picture> pictures = pictureService.save(files);
        Product product = new Product(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getAmount(),
                request.getDiscount()
        );
        productRepository.save(product);
        pictures.forEach(it -> it.setProduct(product));
        return product;
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
    public int getActualPrice(Product product) {
        return product.getPrice() * (100 - product.getDiscount()) / 100;
    }

    @Override
    public double getRating(Product product) {
        double rating = ((double) product.getCommentList().stream()
                .mapToInt(Comment::getRating).sum()) /
                ((double) product.getCommentList().size());
        return !Double.isNaN(rating) ? rating : 0;
    }
}
