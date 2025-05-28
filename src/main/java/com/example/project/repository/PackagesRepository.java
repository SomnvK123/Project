package com.example.project.repository;

import com.example.project.model.Packages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PackagesRepository extends JpaRepository<Packages, Integer> {

    @Query(value = "SELECT count(*) FROM package_products pp JOIN packages p ON pp.package_id = p.id WHERE pp.product_id = :productId AND p.status IN (5, 7, 11) ", nativeQuery = true)
    int countPackagesWithProduct(@Param("productId") Integer productId);

    @Modifying
    @Query(nativeQuery = true, value = """
                UPDATE packages
                SET status = CASE
                    WHEN status = 0 AND ?2 = 3 THEN 3
                    WHEN status = 3 AND ?2 = 5 THEN 5
                    WHEN status = 5 AND ?2 = 7 THEN 7
                    WHEN status = 7 AND ?2 IN (11, 14, -1) THEN ?2
                    WHEN status = 14 AND ?2 IN (11, 20) THEN ?2
                    WHEN status = 11 AND ?2 = 17 THEN 17
                    WHEN status IN (0, 3, 5) AND ?2 = -1 THEN -1
                    ELSE status
                END
                WHERE id = ?1
            """)
    void updatePackageStatus(int id, int newStatus);
}