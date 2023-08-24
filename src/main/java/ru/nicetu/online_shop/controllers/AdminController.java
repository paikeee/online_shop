package ru.nicetu.online_shop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nicetu.online_shop.dto.request.ProductRequest;
import ru.nicetu.online_shop.dto.request.TypeRequest;
import ru.nicetu.online_shop.dto.response.MessageResponse;
import ru.nicetu.online_shop.dto.response.ProductDTO;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.models.Type;
import ru.nicetu.online_shop.services.CommentServiceImpl;
import ru.nicetu.online_shop.services.PictureServiceImpl;
import ru.nicetu.online_shop.services.ProductServiceImpl;
import ru.nicetu.online_shop.services.TypeServiceImpl;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class AdminController {

    private final ProductServiceImpl productService;
    private final TypeServiceImpl typeService;
    private final PictureServiceImpl pictureService;
    private final CommentServiceImpl commentService;

    @PostMapping(path = "product/add")
    public ResponseEntity<ProductDTO> addProduct(@RequestPart("productRequest") @Valid ProductRequest request,
                                                @RequestPart("image") List<MultipartFile> files) {
        Product product = new Product(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getAmount(),
                request.getDiscount()
        );
        productService.save(product);
        pictureService.save(files, product);
        return ResponseEntity.ok(new ProductDTO(product));
    }

    @PostMapping(path = "product/{productId}/settings", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> settings(@PathVariable("productId") int productId,
                                               @RequestBody @Valid ProductRequest settings) {
        Product product = productService.getProduct(productId);
        productService.settings(product, settings);
        return ResponseEntity.ok(new ProductDTO(product));
    }

    @PostMapping(path = "product/{}/settings/{pictureId}/delete")
    public ResponseEntity<?> deletePicture(@PathVariable("pictureId") int pictureId) {
        pictureService.delete(pictureId);
        return ResponseEntity.ok(new MessageResponse("Picture with id " + pictureId + " has been deleted"));
    }

    @PostMapping(path = "/type/add")
    public ResponseEntity<MessageResponse> addType(@RequestBody @Valid TypeRequest request) {
        Type type = new Type(request.getTypeName(), request.getParentId());
        typeService.addType(type, request);
        return ResponseEntity.ok(new MessageResponse("Type added"));
    }

    @PostMapping(path = "/comment/{commentId}/delete")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") int commentId) {
        commentService.delete(commentId);
        return ResponseEntity.ok(new MessageResponse("Comment with id " + commentId + " has been deleted"));
    }

    @PostMapping(path = "/comment/{}/delete/{pictureId}")
    public ResponseEntity<MessageResponse> deleteCommentPicture(@PathVariable("pictureId") int pictureId) {
        pictureService.delete(pictureId);
        return ResponseEntity.ok(new MessageResponse("Picture with id " + pictureId + " has been deleted"));
    }

    @PostMapping(path = "/type/{typeId}/delete")
    public ResponseEntity<MessageResponse> deleteType(@PathVariable("typeId") int typeId) {
        typeService.delete(typeId);
        return ResponseEntity.ok(new MessageResponse("Type with id " + typeId + " has been deleted"));
    }

    @PostMapping(path = "/type/{typeId}/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> addProductsToType(@PathVariable("typeId") int typeId,
                                                             @RequestBody List<Integer> productList) {
        typeService.addProducts(typeId, productList);
        return ResponseEntity.ok(new MessageResponse("Type with id " + typeId + " has been modified"));
    }
}
