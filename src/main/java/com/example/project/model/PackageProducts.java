package com.example.project.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "package_products")
public class PackageProducts extends Auditing{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "package_id")
    private Packages aPackage;

    // Gán với bảng products
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products product;

    private Integer quantity;

}
