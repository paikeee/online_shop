package ru.nicetu.online_shop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nicetu.online_shop.dto.request.ProductDTO;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.dto.response.MessageResponse;
import ru.nicetu.online_shop.services.ProductServiceImpl;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

   private final ProductServiceImpl productService;

   @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> add(@RequestBody ProductDTO product) {
      productService.save(new Product(
              product.getName(),
              product.getPrice(),
              product.getAmount(),
              product.getDiscount(),
              product.getPictureUrl()
      ));
      return ResponseEntity.ok(new MessageResponse("Product has been added"));
   }

   @GetMapping(path = "/all")
   public Iterable<Product> all() {
      return productService.getAllProducts();
   }

}
