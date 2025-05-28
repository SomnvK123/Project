package com.example.project.repository;

import com.example.project.model.PackageProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PackageProductsRepository extends JpaRepository<PackageProducts, Integer> {

}
