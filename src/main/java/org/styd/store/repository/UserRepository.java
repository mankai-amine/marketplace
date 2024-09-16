package org.styd.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.styd.store.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
}
