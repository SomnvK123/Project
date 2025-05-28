package com.example.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ProductInputDto implements Serializable {
    private int productId;
    private String name;
    private String barcode;
    private String image;
    private double weight;
    private double height;
    private double width;
    private double length;
    private double price;
    private Integer status;

    @JsonProperty("users_id")
    private Integer usersId;

    private Integer quantity;
}
