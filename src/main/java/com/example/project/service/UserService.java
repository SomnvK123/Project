package com.example.project.service;

import com.example.project.model.Users;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.project.repository.UsersRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    /**
     * Register a batch of users, skipping existing ones by name.
     * Sets default role and status for new users before saving.
     *
     * @param users List of users to register
     */
    @Transactional
    public void registerBatch(List<Users> users) {
        for (Users user : users) {
            if (usersRepository.findByName(user.getName()) != null) {
                continue; // Skip existing user
            }
            user.setRole("USER");
            user.setStatus(true);
        }
        usersRepository.saveAll(users);
    }

    /**
     * Find a user by their phone number.
     *
     * @param tel Phone number
     * @return User if found, otherwise null
     */
    public Users findByTelephone(String tel) {
        return usersRepository.findByTel(tel);
    }

    /**
     * Authenticate user based on telephone and password.
     *
     * @param tel      Telephone number
     * @param password User password
     * @return User object if credentials are valid, otherwise null
     */
    public Users login(String tel, String password) {
        Users user = usersRepository.findByTel(tel);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null; // Invalid credentials
    }

    /**
     * Find a user by name.
     *
     * @param name User's name
     * @return User if found, otherwise null
     */
    public Users findByName(String name) {
        return usersRepository.findByName(name);
    }

    /**
     * Retrieve all users.
     *
     * @return List of all users
     */
    public List<Users> getAllUsers() {
        return usersRepository.getAllUsers();
    }
}