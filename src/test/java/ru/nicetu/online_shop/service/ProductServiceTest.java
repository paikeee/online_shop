package ru.nicetu.online_shop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nicetu.online_shop.dto.request.ProductRequest;
import ru.nicetu.online_shop.models.Comment;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.repository.ProductRepository;
import ru.nicetu.online_shop.services.PictureServiceImpl;
import ru.nicetu.online_shop.services.ProductServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PictureServiceImpl pictureService;

    @InjectMocks
    private ProductServiceImpl productService;

    private final Product product = new Product("name", "text", 100, 1, 1);

    @Test
    public void testGetProduct() {
        Product expected = product;
        expected.setProductId(1);
        when(productRepository.findByProductId(1)).thenReturn(Optional.of(expected));
        assertEquals(expected, productService.getProduct(1));
        assertThrows(NoSuchElementException.class, () -> productService.getProduct(2));
    }

    @Test
    public void testSave1() {
        ProductRequest request = new ProductRequest(
                "name",
                "text",
                100,
                1,
                1
        );
        when(pictureService.save(any())).thenReturn(new ArrayList<>());
        Product result = productService.save(request, new ArrayList<>());
        assertEquals(product, result);
        verify(productRepository).save(result);
    }

    @Test
    public void testSave2() {
        productService.save(product);
        verify(productRepository).save(product);
    }

    @Test
    public void testSettings() {
        productService.settings(product, new ProductRequest("", "", 1, 1, 1));
        verify(productRepository).save(product);
    }

    @Test
    public void testGetActualPrice() {
        assertEquals(99, productService.getActualPrice(product));
    }

    @Test
    public void testGetRating() {
        List<Comment> commentList = new ArrayList<>();
        commentList.add(new Comment(4, "", null, null));
        commentList.add(new Comment(5, "", null, null));
        product.setCommentList(commentList);
        assertEquals(4.5, productService.getRating(product));
    }
}
