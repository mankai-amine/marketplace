//package org.styd.store.model;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.styd.store.entity.CartItem;
//import org.styd.store.entity.User;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class Cart {
//
//    private User user;
//    // TODO see if we want to initialize with an empty ArrayList
//    private List<CartItem> items = new ArrayList<>();
//
//    public void addItem(CartItem item) {
//        items.add(item);
//    }
//
//    public void removeItem(CartItem item) {
//        items.remove(item);
//    }
//
//    public void updateItem(CartItem item, int amount) {
//        // find the item at index(item), make a copy
//        CartItem toUpdate = items.get(items.indexOf(item));
//        // get and set the new amount
//        int totalAmount = toUpdate.getAmount() + amount;
//        toUpdate.setAmount(totalAmount);
//        // replace the item in the list
//        items.set(items.indexOf(item), toUpdate);
//    }
//
//    public void clearItems() {
//        items.clear();
//    }
//
//    // TODO METHOD FOR PRICE?
//
//
//    // FIXME BELOW MAY BE UNNECESSARY
//    // return CartItem if it exists, otherwise returns null
////    private CartItem findInCart(CartItem item) {
////
////        return null;
////    }
////
////    // return true if item in cart, false otherwise
////    private boolean checkIfInCart(Product product) {
////        return false;
////    }
//}
