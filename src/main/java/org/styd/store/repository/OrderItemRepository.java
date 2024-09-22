package org.styd.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.styd.store.entity.OrderItem;
import org.styd.store.entity.User;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    OrderItem findOrderItemById(Long id);

}

