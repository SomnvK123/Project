package com.example.project.repository;

import com.example.project.dto.PackagesProductDto;
import com.example.project.model.PackageProducts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PackageProductsRepository extends JpaRepository<PackageProducts, Integer> {

    @Query("""
    SELECT new com.example.project.dto.PackagesProductDto(
        p.id,
        p.pickAddress,
        p.customerName,
        p.customerAddress,
        p.customerTel,
        p.pickMoney,
        p.value,
        p.shipMoney,
        p.extraFee,
        p.weight,
        pr.id,
        pr.name,
        pr.barcode,
        pr.price,
        pp.quantity
    )
    FROM Packages p
    JOIN p.packageProducts pp
    JOIN pp.product pr
    ORDER BY p.id
""")
    Page<PackagesProductDto> listPackageProducts(Pageable pageable);
}
