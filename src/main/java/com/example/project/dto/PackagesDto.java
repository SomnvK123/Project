package com.example.project.dto;

import com.example.project.model.Packages;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Packages}
 */
@Getter
@Setter
@AllArgsConstructor

public class PackagesDto implements Serializable {
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
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
    private boolean isDeleted;
    private Integer status;

    @JsonProperty("users_id")
    private Integer usersId;

    private List<ProductInputDto> products;
}