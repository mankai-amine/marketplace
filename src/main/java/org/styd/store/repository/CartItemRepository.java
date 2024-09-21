package org.styd.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.styd.store.entity.Product;
import org.styd.store.entity.User;
import org.styd.store.entity.CartItem;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByBuyer(User buyer);

    List<CartItem> findByBuyerId(Long buyerId);

    void deleteByBuyerId(Long buyerId);

    CartItem findByBuyerAndProduct(User buyer, Product product);

}
