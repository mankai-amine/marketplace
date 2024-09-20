package org.styd.store.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.styd.store.entity.CartItem;
import org.styd.store.entity.Product;
import org.styd.store.entity.User;
import org.styd.store.repository.CartItemRepository;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    private User user;
    private List<CartItem> items;

    public void addItem(CartItem item) {
        items.add(item);
    }

    public void removeItem(CartItem item) {
        items.remove(item);
    }

    // return CartItem if it exists, otherwise returns null
//    private CartItem findInCart(CartItem item) {
//        // TODO
//        return null;
//    }
//
//    // TODO return true if item in cart, false otherwise
//    private boolean checkIfInCart(Product product) {
//        return false;
//    }
}
