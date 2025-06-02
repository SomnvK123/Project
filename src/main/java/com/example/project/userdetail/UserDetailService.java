package com.example.project.userdetail;

import com.example.project.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class UserDetailService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailService.class);

    public String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();

        if (principal instanceof Users) {
            logger.info("Found user: {}", principal);
            Users user = (Users) principal;
            return user.getTel();
        } else if (principal instanceof UserDetails) {
            logger.info("Found UserDetails: {}", principal);
            UserDetails userDetails = (UserDetails) principal;
            return userDetails.getUsername();
        } else {
            return principal.toString();
        }
    }

    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();

        if (principal instanceof Users) {
            Users user = (Users) principal;
            return user.getId();
        } else {
            return null;
        }
    }
}