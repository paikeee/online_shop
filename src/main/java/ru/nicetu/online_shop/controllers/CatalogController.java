package ru.nicetu.online_shop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nicetu.online_shop.dto.request.CommentRequest;
import ru.nicetu.online_shop.dto.request.FilterRequest;
import ru.nicetu.online_shop.dto.response.CommentResponse;
import ru.nicetu.online_shop.dto.response.ProductDTO;
import ru.nicetu.online_shop.dto.response.TypeProductsDTO;
import ru.nicetu.online_shop.models.Comment;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.services.*;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/catalog")
@AllArgsConstructor
public class CatalogController {


    private final TypeServiceImpl typeService;
    private final ProductServiceImpl productService;
    private final CommentServiceImpl commentService;
    private final FilterServiceImpl filterService;


    @GetMapping(path = "/types")
    public ResponseEntity<?> getTypes() {
        return ResponseEntity.ok(typeService.getTypes());
    }

    @GetMapping(path = "/{typeId}")
    public ResponseEntity<TypeProductsDTO> getProducts(@PathVariable("typeId") int typeId) {
        return ResponseEntity.ok(typeService.buildTypeProducts(typeId));
    }

    @GetMapping(path = "/product/{productId}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable("productId") int productId) {
        Product product = productService.getProduct(productId);
        return ResponseEntity.ok(new ProductDTO(
                product,
                productService.getActualPrice(product),
                productService.getRating(product)
        ));
    }

    @PostMapping(path = "/product/{productId}/comment/add")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> addComment(@PathVariable("productId") int productId,
                                        @RequestPart("request") @Valid CommentRequest request,
                                        @RequestPart("image") List<MultipartFile> files) {
        Comment comment = commentService.addComment(productId, request, files);
        return ResponseEntity.ok(new CommentResponse(
                comment.getCommentId(),
                productId,
                comment.getRating(),
                comment.getText()
        ));
    }

    @PostMapping(path = "/{typeId}/filter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TypeProductsDTO> filterProducts(@PathVariable("typeId") int typeId,
                                                          @RequestBody FilterRequest request) {
        return ResponseEntity.ok(filterService.buildFilteredType(typeId, request));
    }
}
