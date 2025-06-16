package com.example.helloboot.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.helloboot.entity.User;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(
        String username
    );
    boolean existsByUsername(
        String username
    );
    Optional<User> findByToken(
        String token
    );
    boolean existsByToken(
        String token
    );
}