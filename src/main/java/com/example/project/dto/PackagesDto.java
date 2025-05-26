package com.example.project.dto;

import com.example.project.model.Packages;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    private String name;
    private String barcode;
    private String image;
    private double weight;
    private double height;
    private double width;
    private double length;
    private double price;
    private boolean isDeleted;
    private Integer status;
}