package ru.nicetu.online_shop.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nicetu.online_shop.dto.request.*;
import ru.nicetu.online_shop.dto.response.AttributesDTO;
import ru.nicetu.online_shop.dto.response.MessageResponse;
import ru.nicetu.online_shop.dto.response.ProductDTO;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.services.*;

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
    private final AttributeServiceImpl attributeService;

    @PostMapping(path = "product/add")
    public ResponseEntity<ProductDTO> addProduct(@RequestPart("productRequest") @Valid ProductRequest request,
                                                @RequestPart("image") List<MultipartFile> files) {
        Product product = productService.save(request, files);
        return ResponseEntity.ok(new ProductDTO(
                product,
                productService.getActualPrice(product),
                productService.getRating(product)
        ));
    }

    @PostMapping(path = "product/{productId}/settings", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> settings(@PathVariable("productId") int productId,
                                               @RequestBody @Valid ProductRequest settings) {
        Product product = productService.getProduct(productId);
        productService.settings(product, settings);
        return ResponseEntity.ok(new ProductDTO(
                product,
                productService.getActualPrice(product),
                productService.getRating(product)
        ));
    }

    @PostMapping(path = "product/{}/settings/{pictureId}/delete")
    public ResponseEntity<?> deletePicture(@PathVariable("pictureId") int pictureId) {
        pictureService.delete(pictureId);
        return ResponseEntity.ok(new MessageResponse("Picture with id " + pictureId + " has been deleted"));
    }

    @PostMapping(path = "/type/add")
    public ResponseEntity<MessageResponse> addType(@RequestBody @Valid TypeRequest request) {
        typeService.addType(request);
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
                                                             @RequestBody @Valid AddProductsToTypeRequest productList) {
        typeService.addProducts(typeId, productList.getProductList());
        return ResponseEntity.ok(new MessageResponse("Type with id " + typeId + " has been modified"));
    }

    @PostMapping(path = "/type/{typeId}/attribute", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> addAttributesType(@PathVariable("typeId") int typeId,
                                                             @RequestBody AttributeRequest request) {
        attributeService.saveAttributes(typeId, request.getNameList());
        return ResponseEntity.ok(new MessageResponse("Attributes have been added to type with id " + typeId));
    }

    @PostMapping(path = "/attribute/{attributeId}/delete")
    public ResponseEntity<MessageResponse> deleteAttribute(@PathVariable("attributeId") int attributeId) {
        attributeService.deleteAttribute(attributeId);
        return ResponseEntity.ok(new MessageResponse("Attribute with id " + attributeId + " deleted"));
    }

    @PostMapping(path = "product/{productId}/settings/attribute", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> addAttributesProduct(@PathVariable("productId") int productId,
                                                                @RequestBody String request) throws JsonProcessingException {
        attributeService.saveProductAttributes(productId, new AttributeValueRequest(request).getValues());
        return ResponseEntity.ok(new MessageResponse("Attributes have been added to product with id " + productId));
    }

    @GetMapping(path = "product/{productId}/settings/attribute")
    public ResponseEntity<List<AttributesDTO>> getAttributesProduct(@PathVariable("productId") int productId) {
        return ResponseEntity.ok(attributeService.buildAttributesDTO(
                productService.getProduct(productId).getTypeList()
        ));
    }
}
