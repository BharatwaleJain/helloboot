package com.example.helloboot.controller;
import com.example.helloboot.dto.*;
import com.example.helloboot.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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

    // Login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest body) {
        LoginResponse resp = service.login(body);
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
        if (success) return HttpStatus.OK;
        if (token == null || token.isBlank())
            return HttpStatus.BAD_REQUEST;
        return HttpStatus.UNAUTHORIZED;
    }
}