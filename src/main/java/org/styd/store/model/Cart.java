package org.styd.store.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.styd.store.entity.CartItem;
import org.styd.store.entity.User;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private User user;
    private List<CartItem> items;

    public void addItem(CartItem item) {

    }
}
