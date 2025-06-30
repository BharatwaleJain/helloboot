package com.example.helloboot.dto;

public class ListItemResponse {
    private Long id;
    private String title;
    private String category;
    private String userName;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;

    public ListItemResponse(Long id, String title, String category, String userName, java.time.LocalDateTime createdAt, java.time.LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.userName = userName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getUserName() {
        return userName;
    }

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public java.time.LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}