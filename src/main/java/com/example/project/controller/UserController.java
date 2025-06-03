package com.example.project.controller;

import com.example.project.config.JwtFilter;
import com.example.project.config.JwtUtil;
import com.example.project.dto.AuthDto;
import com.example.project.model.Users;
import com.example.project.repository.UsersRepository;
import com.example.project.service.UserService;
import com.example.project.userdetail.UserDetailService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserDetailService userDetailService;

    private final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Users users) {
        Users existingUser = userService.findByName(users.getName());
        if (existingUser != null) {
            return ResponseEntity.status(409).body("User already exists");
        }
        users.setRole("USER");
        users.setStatus(true);
        userService.registerBatch(List.of(users));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                "User registered successfully"
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDto authDto) {
        Users user = userService.login(authDto.getTel(), authDto.getPassword());
        if (user != null) {
            String token = jwtUtil.generateJwtToken(user);
            logger.info("User {} logged in successfully", user.getTel());

            AuthDto response = new AuthDto(token, user.getTel(), user.getPassword(), user.getName(), user.getAddress(), user.isStatus(), user.getRole());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/current-user")
    public ResponseEntity<Users> getCurrentUser() {
        String username = userDetailService.getCurrentUser();
        if (username != null) {
            Users user = usersRepository.findByTel(username);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}