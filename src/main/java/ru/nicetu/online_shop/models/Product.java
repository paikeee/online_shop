package ru.nicetu.online_shop.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "product")
public class Product {

    public Product(String name,
                   int price,
                   int amount,
                   int discount,
                   String pictureUrl) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.discount = discount;
        this.pictureUrl = pictureUrl;
    }

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "amount")
    private int amount;

    @Column(name = "discount")
    private int discount;

    @Column(name = "picture_url")
    private String pictureUrl;

    @Transient
    public int getActualPrice() {
        return getPrice() * (100 - getDiscount()) / 100;
    }

}
