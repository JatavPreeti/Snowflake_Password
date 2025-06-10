package com.example.demo.model;

public class RotationResponse {
    private boolean success;
    private String message;

    public RotationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    //Represent the result of password rotation (Success Failure message)
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}

