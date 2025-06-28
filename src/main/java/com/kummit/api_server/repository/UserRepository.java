package com.kummit.api_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kummit.api_server.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * User 엔티티에 대한 CRUD 기능을 제공하는 JPA Repository 인터페이스
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Page<User> findAllByOrderByRatingDesc(Pageable pageable);
}
