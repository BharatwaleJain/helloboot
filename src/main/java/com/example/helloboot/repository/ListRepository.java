package com.example.helloboot.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.helloboot.entity.ListItem;
public interface ListRepository extends JpaRepository<ListItem, Long> { }