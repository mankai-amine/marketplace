package org.styd.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.styd.store.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // TODO Figure out which of these we need as we develop the app
    // FIXME These may also need to have a second parameter for isDeleted
    // TODO research Pageable

    Product findProductById(Long id);

    List<Product> findBySellerId(Long sellerId);

//    List<Product> findByName(String productName);
//
//    List<Product> findByNameContaining(String productName);
//
//    List<Product> findByNameContainingIgnoreCase(String productName);
//
//    List<Product> findBySellerId(Long sellerId);
//
//    List<Product> findByCategoryId(Long categoryId);
//
//    List<Product> findAllDeletedProducts();
        // FIXIT custom code to only search for isDeleted
}
