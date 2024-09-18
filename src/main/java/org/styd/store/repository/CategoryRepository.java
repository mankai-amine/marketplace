package org.styd.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.styd.store.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

}
