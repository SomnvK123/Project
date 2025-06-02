package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor

public class PackagesProductDto implements Serializable {
    private int package_id;
    private String pickAddress;           // pick_address
    private String customerName;          // customer_name
    private String customerAddress;       // customer_address
    private String customerTel;           // customer_tel
    private Double pickMoney;             // tiền thu hộ
    private Double value;                 // Tổng giá trị hàng hóa
    private Double shipMoney;             // phí vận chuyển
    private Double extraFee;              // phụ phí vận chuyển
    private Double weight;

    private int product_id;
    private String name;
    private String barcode;
    private Double price;

    private Integer quantity;

}
