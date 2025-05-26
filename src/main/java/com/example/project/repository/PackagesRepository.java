package com.example.project.repository;

import com.example.project.model.Packages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PackagesRepository extends JpaRepository<Packages, Integer> {

    @Query(value = "SELECT count(*) FROM package_products pp JOIN packages p ON pp.package_id = p.id WHERE pp.product_id = :productId AND p.status IN (5, 7, 11) ", nativeQuery = true)
    int countPackagesWithProduct(@Param("productId") Integer productId);
}