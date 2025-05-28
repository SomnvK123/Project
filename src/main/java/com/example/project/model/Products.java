package com.example.project.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Products extends Auditing{
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
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


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "users_id")
    private Users users;

    @OneToMany(mappedBy = "product")
    private List<PackageProducts> packageProducts;
}
