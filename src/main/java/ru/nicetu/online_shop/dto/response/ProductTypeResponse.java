package ru.nicetu.online_shop.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.nicetu.online_shop.models.Picture;
import ru.nicetu.online_shop.models.Product;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
public class ProductTypeResponse {

    private final int productId;
    private final String name;
    private final String description;
    private final int price;
    private final int actualPrice;
    private final int amount;
    private final int discount;
    private final List<byte[]> pictures;
    private final double rating;

    public ProductTypeResponse(Product product, int actualPrice, double rating) {
        this.productId = product.getProductId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.actualPrice = actualPrice;
        this.amount = product.getAmount();
        this.discount = product.getDiscount();
        this.pictures = product.getPictureList().stream()
                .map(Picture::getImage)
                .collect(Collectors.toList());
        this.rating = rating;
    }

}
