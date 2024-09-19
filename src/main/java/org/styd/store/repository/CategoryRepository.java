package org.styd.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.styd.store.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

}
