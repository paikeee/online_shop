package ru.nicetu.online_shop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nicetu.online_shop.dto.request.CommentRequest;
import ru.nicetu.online_shop.dto.response.CommentResponse;
import ru.nicetu.online_shop.dto.response.ProductDTO;
import ru.nicetu.online_shop.dto.response.ProductTypeResponse;
import ru.nicetu.online_shop.dto.response.TypeProductsDTO;
import ru.nicetu.online_shop.models.Comment;
import ru.nicetu.online_shop.services.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/catalog")
@AllArgsConstructor
public class CatalogController {


    private final TypeServiceImpl typeService;
    private final ProductServiceImpl productService;
    private final CommentServiceImpl commentService;
    private final PersonDetailsService personDetailsService;
    private final PictureServiceImpl pictureService;

    @GetMapping(path = "/types")
    public ResponseEntity<?> getTypes() {
        return ResponseEntity.ok(typeService.getTypes());
    }

    @GetMapping(path = "/{typeId}")
    public ResponseEntity<TypeProductsDTO> getProducts(@PathVariable("typeId") int typeId) {
        return ResponseEntity.ok(new TypeProductsDTO(
                typeService.getProductsByType(typeId).stream()
                        .map(ProductTypeResponse::new)
                        .collect(Collectors.toList())));
    }

    @GetMapping(path = "/product/{productId}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable("productId") int productId) {
        return ResponseEntity.ok(new ProductDTO(productService.getProduct(productId)));
    }

    @PostMapping(path = "/product/{productId}/comment/add")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> addComment(@PathVariable("productId") int productId,
                                        @RequestPart("request") @Valid CommentRequest request,
                                        @RequestPart("image") List<MultipartFile> files) {
        Comment comment = new Comment(
                request.getRating(),
                request.getText(),
                productService.findById(productId),
                personDetailsService.currentUser()
        );
        commentService.save(comment);
        if (files.size() != 1 || !Objects.equals(files.get(0).getOriginalFilename(), "")) {
            comment.setPictures(pictureService.save(files, comment));
        }
        return ResponseEntity.ok(new CommentResponse(
                comment.getCommentId(),
                productId,
                comment.getRating(),
                comment.getText()
        ));
    }
}
