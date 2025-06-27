package com.example.helloboot.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
// @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "list")
public class ListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private String title;
    @Column(length = 7,  nullable = false)
    private String category;
    @Column(name = "created_at",  nullable = false, columnDefinition = "timestamp default current_timestamp")
    private LocalDateTime createdAt;
    @Column(name = "updated_at",  nullable = false, columnDefinition = "timestamp default current_timestamp")
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public ListItem() { }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}