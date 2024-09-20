//package org.styd.store.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.styd.store.entity.CartItem;
//import org.styd.store.entity.Product;
//import org.styd.store.entity.User;
//import org.styd.store.repository.CartItemRepository;
//
//@Service
//public class CartService {
//
//    @Autowired
//    private CartItemRepository cartItemRepository;
//
//    public CartItem addItemToCart(User user, Product product, int amount) {
//       CartItem existingItem = cartItemRepository.findByUserAndProduct(user, product);
//       if (existingItem != null) {
//           existingItem.setAmount(existingItem.getAmount() + amount);
//           cartItemRepository.save(existingItem);
//       }
//    }
//
//}
