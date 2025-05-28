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

    private String pickAddress;           // pick_address
    private String customerName;          // customer_name
    private String customerAddress;       // customer_address
    private String customerTel;           // customer_tel
    private Double pickMoney;             // tiền thu hộ
    private Double value;                 // Tổng giá trị hàng hóa
    private Double shipMoney;             // phí vận chuyển
    private Double extraFee;              // phụ phí vận chuyển
    private Double weight;
    private boolean isDeleted = false;
    private Integer status;

    // Additional fields or methods can be added here if necessary
    @OneToMany(mappedBy = "aPackage")
    private List<PackageProducts> packageProducts;
}
