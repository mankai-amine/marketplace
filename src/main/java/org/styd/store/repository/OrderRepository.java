package org.styd.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.styd.store.entity.Order;
import org.styd.store.entity.User;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findOrderById(Long id);

    List<Order> findByBuyer(User buyer);

}
