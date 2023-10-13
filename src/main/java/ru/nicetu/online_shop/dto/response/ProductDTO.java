package ru.nicetu.online_shop.dto.response;

import lombok.Getter;
import ru.nicetu.online_shop.models.Picture;
import ru.nicetu.online_shop.models.Product;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProductDTO {

    private final int productId;
    private final String name;
    private final String description;
    private final int price;
    private final int actualPrice;
    private final int amount;
    private final int discount;
    private final List<byte[]> pictures;
    private final double rating;
    private final List<CommentDTO> comments;

    public ProductDTO(Product product, int actualPrice, double rating) {
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
        this.comments = product.getCommentList().stream()
                .map(it -> new CommentDTO(
                        it.getPerson().getName() + " " + it.getPerson().getSurname(),
                        it.getRating(),
                        it.getText(),
                        it.getPictures().stream()
                                .map(Picture::getImage)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

}
