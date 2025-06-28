package com.kummit.api_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kummit.api_server.domain.User;

import java.util.Optional;

/**
 * User 엔티티에 대한 CRUD 기능을 제공하는 JPA Repository 인터페이스
 */
public interface UserRepository extends JpaRepository<User, Long> {
    // 필요하다면 아래와 같이 커스텀 쿼리 메서드를 추가할 수 있습니다.
    Optional<User> findByUsername(String username);
}
