package com.zhenyuye.webapp.repositories;

import com.zhenyuye.webapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findUserByUsername(String username);
    User findByEmail(String email);
    boolean existsByEmail(String email);

}
