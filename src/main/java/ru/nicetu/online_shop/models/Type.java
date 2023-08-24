package ru.nicetu.online_shop.models;

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
@Table(name = "type")
public class Type {

    public Type(String name, int parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    @Id
    @Column(name = "type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int typeId;

    @Column(name = "name")
    private String name;

    @Column(name = "parent_id")
    private int parentId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_type",
            joinColumns = @JoinColumn(name = "type_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> productList = new ArrayList<>();
}
