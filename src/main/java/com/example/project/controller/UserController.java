package com.example.project.controller;

import com.example.project.config.JwtUtil;
import com.example.project.dto.AuthDto;
import com.example.project.model.Users;
import com.example.project.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Users users) {
        Users existingUser = userService.findByName(users.getName());
        if (existingUser != null) {
            return ResponseEntity.status(409).body("User already exists");
        }
        users.setRole("USER");
        users.setStatus(true);
        userService.batchRegister(List.of(users));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                "User registered successfully"
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users users) {
        Users existingUser = userService.findByTel(users.getTel());
        if (existingUser == null || !existingUser.getPassword().equals(users.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        String token = jwtUtil.generateJwtToken(existingUser);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        AuthDto authDto = new AuthDto(token,
                existingUser.getName(),
                existingUser.getRole(),
                existingUser.getTel(),
                existingUser.getPassword(),
                existingUser.isStatus(),
                existingUser.getRole());
        return ResponseEntity.status(HttpStatus.OK)
                .body(authDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}