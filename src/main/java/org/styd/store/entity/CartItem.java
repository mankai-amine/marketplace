package org.styd.store.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int amount;

    public CartItem(User buyer, Product product, int amount) {
        this.buyer = buyer;
        this.product = product;
        this.amount = amount;
    }

    // The methods below enable Set comparison for other purposes in our application
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem)) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(getBuyer(), cartItem.getBuyer()) &&
                Objects.equals(getProduct(), cartItem.getProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBuyer(), getProduct());
    }
}
