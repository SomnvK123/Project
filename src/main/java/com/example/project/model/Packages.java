package com.example.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "packages")
public class Packages extends Auditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String barcode;
    private String image;
    private double weight;
    private double height;
    private double width;
    private double length;
    private double price;
    private boolean isDeleted = false;
    private Integer status;

    // Additional fields or methods can be added here if necessary
    @OneToMany(mappedBy = "aPackage")
    private List<PackageProducts> packageProducts;
}
