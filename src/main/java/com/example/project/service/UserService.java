package com.example.project.service;

import com.example.project.model.Users;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.project.repository.UsersRepository;

import java.util.List;

@Service
public class UserService  {

    @Autowired
    private UsersRepository usersRepository;


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

    public Users login(String tel, String password) {
        Users user = usersRepository.findByTel(tel);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null; // Invalid credentials
    }

    public Users findByName(String name) {
        return usersRepository.findByName(name);
    }

    public List<Users> getAllUsers() {
        return usersRepository.getAllUsers();
    }
}
