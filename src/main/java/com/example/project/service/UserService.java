package com.example.project.service;

import com.example.project.model.Users;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.example.project.repository.UsersRepository;

import java.util.List;

@Service
public class UserService  {

    @Autowired
    private UsersRepository usersRepository;

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserService.class);

    @Transactional
    public void batchRegister (List<Users> users) {
        for(Users user : users) {
            Users existingUser = usersRepository.findByName(user.getName());
            if (existingUser != null) {
                continue;
            }
            user.setRole("USER");
            user.setStatus(true);
        }
        usersRepository.saveAll(users);
    }

    public Users findByTel(String tel) {
        return usersRepository.findByTel(tel);
    }

    public Users findByName(String name) {
        return usersRepository.findByName(name);
    }

    public List<Users> getAllUsers() {
        return usersRepository.getAllUsers();
    }

    public String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof Users) {
            logger.info("Found user: {}", principal);
            Users user = (Users) principal;
            return user.getName();
        } else if (principal instanceof UserDetails) {
            logger.info("Found UserDetails: {}", principal);
            UserDetails userDetails = (UserDetails) principal;
            return userDetails.getUsername();
        } else {
            return principal.toString();
        }
    }
}
