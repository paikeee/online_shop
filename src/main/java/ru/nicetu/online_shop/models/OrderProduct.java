package ru.nicetu.online_shop.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "order_product")
public class OrderProduct {

    @EmbeddedId
    @JsonIgnore
    private OrderProductPK pk;

    @Column
    private int quantity;

    public OrderProduct(Order order, Product product, int quantity) {
        pk = new OrderProductPK();
        pk.setOrder(order);
        pk.setProduct(product);
        this.quantity = quantity;
    }

    @Transient
    public Product getProduct() {
        return this.pk.getProduct();
    }

    @Transient
    public int getTotalPrice() {
        return getProduct().getActualPrice() * getQuantity();
    }

    @Override
    public String toString() {
        return getProduct().getName() + " * " + getQuantity() + " = " + getTotalPrice() + " RUB";
    }
}
