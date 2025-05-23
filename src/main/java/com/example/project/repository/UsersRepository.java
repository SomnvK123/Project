package com.example.project.repository;

import com.example.project.model.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsersRepository extends JpaRepository<Users, Integer> {

  @Query(nativeQuery = true, value = "SELECT * FROM users WHERE name = ?1")
  Users findByName(String name);

  @Modifying
  @Transactional
  @Query("Insert into Users (tel, password, name, address, status, role) values (?1, ?2, ?3, ?4, ?5, ?6)")
  void batchRegister(List<Users> users);

  @Query("SELECT u FROM Users u WHERE u.status = ?1")
  List<Users> findAllByStatus(boolean status);

  @Query("SELECT u FROM Users u WHERE u.tel = ?1")
  Users findByTel(String tel);

  @Query("SELECT u FROM Users u")
  List<Users> getAllUsers();
}