package com.example.project.dto;

import com.example.project.model.Products;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link Products}
 */
@Getter
@Setter
@AllArgsConstructor
public class ProductsDto implements Serializable {
    @NotBlank(message = "Not Blank")
    private String name;
    @NotBlank(message = "Not Blank")
    private String barcode;
    private String image;
    private double weight;
    private double height;
    private double width;
    private double length;
    private double price;
    private boolean isDeleted;
    private boolean status;
}