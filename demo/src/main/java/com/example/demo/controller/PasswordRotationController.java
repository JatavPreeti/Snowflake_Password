package com.example.demo.controller;
import com.example.demo.service.PasswordRotationService;
import com.example.demo.model.RotationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PasswordRotationController {
    @Autowired
    private PasswordRotationService rotationService;

    @PostMapping("/rotate-password")
    public RotationResponse rotatePassword() {
        boolean success = rotationService.rotatePassword();
        return new RotationResponse(success, success ? "Password rotated successfully!" : "Rotation failed.");
    }
}
