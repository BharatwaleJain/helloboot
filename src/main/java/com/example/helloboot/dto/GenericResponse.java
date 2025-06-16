package com.example.helloboot.dto;
public class GenericResponse {
    private final boolean success;
    private final String  message;
    public GenericResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    public boolean isSuccess() {
        return success;
    }
    public String  getMessage() {
        return message;
    }
}