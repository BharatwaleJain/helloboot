package com.example.helloboot.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.helloboot.entity.ListItem;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface ListRepository extends JpaRepository<ListItem, Long> {
    Page<ListItem> findByCategory(
        String category,
        Pageable pageable
    );
    Page<ListItem> findByCategoryAndTitleContainingIgnoreCase(
        String category,
        String title,
        Pageable pageable
    );
    Page<ListItem> findByCategoryAndCreatedAtBetween(
        String category,
        LocalDateTime start,
        LocalDateTime end,
        Pageable pageable
    );
    Page<ListItem> findByCategoryAndTitleContainingIgnoreCaseAndCreatedAtBetween(
        String category,
        String title,
        LocalDateTime start,
        LocalDateTime end,
        Pageable pageable
    );
}