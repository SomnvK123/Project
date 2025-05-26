package com.example.project.repository;

import com.example.project.model.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM products WHERE name = ?1")
    Products findByName(String name);

    Products findById(int id);

    void deleteById(int id);

    Page<Products> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM products WHERE barcode = ?1")
    Products findByBarcode(String barcode);

}