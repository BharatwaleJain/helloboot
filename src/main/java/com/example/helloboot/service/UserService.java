package com.example.helloboot.service;
import com.example.helloboot.dto.*;
import com.example.helloboot.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.Base64;
@Service
public class UserService {
    private final UserRepository repo;
    private final SecureRandom random = new SecureRandom();
    public UserService(UserRepository repo) {
        this.repo = repo;
    }
    // Login
    public LoginResponse login(LoginRequest req) {
        return repo.findByUsername(req.getUsername())
            .filter(u -> u.getPassword().equals(req.getPassword()))
            .map(u -> {
                String token = generateToken();
                u.setToken(token);
                repo.save(u);
                return new LoginResponse(
                    true,
                    "Login Successful",
                    token
                );
            })
            .orElseGet(() ->
                new LoginResponse(
                    false,
                    "Incorrect Credential, Try Again",
                    null
                )
            );
    }
    // Logout
    public GenericResponse logout(String token ) {
        if (token == null || token.isBlank())
            return new GenericResponse(
                false,
                "Session Expired"
            );
        return repo.findByToken(token)
            .map(u -> {
                repo.save(u);
                return new GenericResponse(
                    true,
                    "Logged Out Successfully"
                );
            })
            .orElseGet(() ->
                new GenericResponse(
                    false,
                    "Session Expired"
                )
            );
    }
    // Check
    public GenericResponse check(String token) {
        if (token == null || token.isBlank())
            return new GenericResponse(
                false,
                "No Token Provided"
            );
        boolean valid = repo.findByToken(token).isPresent();
        return valid ? new GenericResponse(
            true,
            "Session Authenticated"
        ) : new GenericResponse(
            false,
            "Session Expired"
        );
    }
    // Helper
    private String generateToken() {
        byte[] bytes = new byte[40];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}