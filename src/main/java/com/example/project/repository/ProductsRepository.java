package com.example.project.repository;

import com.example.project.dto.ProductsDto;
import com.example.project.model.Products;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM products WHERE name = ?1")
    Optional<Products> findByName(String name);

    Page<Products> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM products WHERE barcode = ?1")
    Optional<Products> findByBarcode(String barcode);
}