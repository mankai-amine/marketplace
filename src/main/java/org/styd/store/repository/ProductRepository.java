package org.styd.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.styd.store.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findProductById(Long id);

    List<Product> findBySellerId(Long sellerId);

    List<Product> findByIsDeletedFalse();

}
