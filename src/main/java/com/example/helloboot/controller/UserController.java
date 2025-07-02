package com.example.helloboot.controller;

import com.example.helloboot.dto.*;
import com.example.helloboot.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/")
@CrossOrigin(originPatterns = "*")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // Fetch all users (for admin)
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = service.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Fetch a user by ID (for admin)
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = service.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    // Create a new user (for admin)
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        try {
            UserDto createdUser = service.createUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Failed to create user"));
        }
    }

    // Enable/disable login (for admin)
    @PutMapping("/users/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {
        boolean updated = service.updateUserStatus(id);
        if (updated)
            return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }

    // Update a user (for admin)
    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        UserDto updatedUser = service.updateUser(id, userDto);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    // Delete user (for admin)
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean deleted = service.deleteUser(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "User not found"));
        }
        return ResponseEntity.ok(Map.of("success", true, "message", "User deleted successfully"));
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest body) {
        LoginResponse resp = service.login(body);
        return resp.isSuccess()
                ? ResponseEntity.ok(resp)
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
    }
    @PostMapping("/login2")
    public ResponseEntity<LoginResponse> login2(@RequestBody LoginRequest body) {
        LoginResponse resp = service.login2(body);
        return resp.isSuccess()
                ? ResponseEntity.ok(resp)
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
    }

    // Logout
    @PostMapping("/logout")
    public ResponseEntity<GenericResponse> logout(@RequestBody TokenRequest body) {
        GenericResponse resp = service.logout(body.getToken());
        HttpStatus status = getStatus(body.getToken(), resp.isSuccess());
        return new ResponseEntity<>(resp, status);
    }

    // Check
    @PostMapping("/check")
    public ResponseEntity<GenericResponse> check(@RequestBody TokenRequest body) {
        GenericResponse resp = service.check(body.getToken());
        HttpStatus status = getStatus(body.getToken(), resp.isSuccess());
        return new ResponseEntity<>(resp, status);
    }

    private HttpStatus getStatus(String token, boolean success) {
        if (success)
            return HttpStatus.OK;
        if (token == null || token.isBlank())
            return HttpStatus.BAD_REQUEST;
        return HttpStatus.UNAUTHORIZED;
    }
}