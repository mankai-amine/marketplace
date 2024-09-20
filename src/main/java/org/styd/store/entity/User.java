package org.styd.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    public enum Role {
        ADMIN,
        SELLER,
        BUYER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="Username is required")
    @Column(unique = true)
    @Size(min = 4, max = 20, message= "Username must contain between {min} and {max} characters")
    @Pattern(regexp = "^[a-z0-9]+$", message = "Username must only consist of lower case letters and numbers")
    private String username;

    @Email
    @NotBlank(message="Email is required")
    @Column(unique = true, length = 230)
    private String email;

    @NotBlank(message="Password is required")
    @Size(min = 6, max = 100, message= "Password must contain between {min} and {max} characters")
    private String password;

    @Transient
    private String password2;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String address;

    private String picture;

    @Column(name = "credit_card")
    //@Size(min = 14, max = 16, message = "Credit card number must be between 14 and 16 digits")
    //@Pattern(regexp = "\\d*", message = "Credit card number must contain only digits")
    private String creditCard;


    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders = new HashSet<>();;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Set<CartItem> cartItems;

//  ...= new HashSet<>() avoids product set not being initialized/fetched when running .get() on a user
    //  ...= new HashSet<>() avoids product set not being initialized/fetched when running .get() on a user
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();

    //@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //private Set<Order> orders;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CartItem> cartItems = new HashSet<>();

    ///// Cart methods below /////

    public void addToCart(Product product, int amount) {
        CartItem checkExisting = findCartItem(product);
        if (checkExisting != null) {
            // in controller, check the stock amount of the product
            checkExisting.setAmount(checkExisting.getAmount() + amount);
        } else {
            cartItems.add(new CartItem(this, product, amount));
        }
    }

    // this will check to make sure the item is actually in the cart before it goes to remove it
    public void removeFromCart(Product product) {
        cartItems.removeIf(item -> item.getProduct().equals(product));
    }

    public void clearCart() {
        cartItems.clear();
    }

    public CartItem findCartItem(Product product) {
        return cartItems.stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst()
                .orElse(null);
    }
    // FIXME code below is the same as above, only for reference
    //        for (CartItem item : cartItems) {
//            if (item.getProduct().equals(product)) {
//                return item;
//            }
//        }
//        return null;
}

