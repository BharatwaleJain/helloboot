package com.example.helloboot.dto;
import java.util.List;
public class LoginResponse {
    private final boolean success;
    private final String  message;
    private final String  token;
    private Boolean admin;
    private List<String> permission;
    public LoginResponse(boolean success, String message, String token, Boolean admin, List<String> permission) {
        this.success = success;
        this.message = message;
        this.token   = token;
        this.admin = admin;
        this.permission = permission;
    }
    public boolean isSuccess() {
        return success;
    }
    public String  getMessage() {
        return message;
    }
    public String  getToken() {
        return token;
    }
    public Boolean getAdmin() {
        return admin;
    }
    public List<String> getPermission() {
        return permission;
    }
}