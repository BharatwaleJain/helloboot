package com.example.helloboot.entity;

import jakarta.persistence.*;
import java.util.Arrays;
import java.util.List;
import com.example.helloboot.converter.StringListConverter;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, unique = true, nullable = false)
    private String username;
    @Column(length = 25, nullable = false)
    private String name;
    @Column(length = 100, nullable = false)
    private String password;
    @Column(length = 100, unique = true, nullable = false)
    private String token;
    @Column(nullable = false)
    private boolean admin;
    @Column(name = "permission", columnDefinition = "text")
    @Convert(converter = StringListConverter.class)
    private List<String> permission;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
        if (admin) {
            this.setPermission(Arrays.asList("task", "read", "pending"));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPermission() {
        return permission;
    }

    public void setPermission(List<String> permission) {
        this.permission = permission;
        if (admin) {
            this.setPermission(Arrays.asList("task", "read", "pending"));
        }
    }
}