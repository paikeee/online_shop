package ru.nicetu.online_shop.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "product")
public class Product {

    public Product(String name,
                   String description,
                   int price,
                   int amount,
                   int discount) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
        this.discount = discount;
    }

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private int price;

    @Column(name = "amount")
    private int amount;

    @Column(name = "discount")
    private int discount;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "product_picture",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "picture_id"))
    private List<Picture> pictureList = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_comment",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id"))
    private List<Comment> commentList = new ArrayList<>();

    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_type",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id"))
    private List<Type> typeList = new ArrayList<>();

    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_attribute",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "value_id"))
    private List<AttributeValue> attributeValues = new ArrayList<>();

    @Transient
    public int getActualPrice() {
        return getPrice() * (100 - getDiscount()) / 100;
    }

    @Transient
    public double getRating() {
        double rating = ((double) commentList.stream()
                .mapToInt(Comment::getRating).sum()) /
                ((double) commentList.size());
        return !Double.isNaN(rating) ? rating : 0;
    }
}
