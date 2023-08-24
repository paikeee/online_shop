package ru.nicetu.online_shop.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "picture")
public class Picture {

    public Picture(byte[] image, Product product) {
        this.image = image;
        this.product = product;
    }

    public Picture(byte[] image, Comment comment) {
        this.image = image;
        this.comment = comment;
    }

    @Id
    @Column(name = "picture_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pictureId;

    @Column(name = "image")
    private byte[] image;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "product_picture",
            joinColumns = @JoinColumn(name = "picture_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "comment_picture",
            joinColumns = @JoinColumn(name = "picture_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id"))
    private Comment comment;

}
