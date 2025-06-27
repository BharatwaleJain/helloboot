package com.example.helloboot.dto;
import java.util.List;
public class GenericResponse {
    private final boolean success;
    private final String  message;
    private Boolean admin;
    private List<String> permission;
    public GenericResponse(boolean success, String message, Boolean admin, List<String> permission) {
        this.success = success;
        this.message = message;
        this.admin = admin;
        this.permission = permission;
    }
    public boolean isSuccess() {
        return success;
    }
    public String getMessage() {
        return message;
    }
    public Boolean getAdmin() {
        return admin;
    }
    public List<String> getPermission() {
        return permission;
    }
}