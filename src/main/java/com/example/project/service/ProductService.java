package com.example.project.service;

import com.example.project.dto.ProductsDto;
import com.example.project.model.Products;
import com.example.project.repository.ProductsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class ProductService {

    @Autowired
    private ProductsRepository productsRepository;

    public Page<Products> getAllProducts(Pageable pageable) {
        return productsRepository.findAll(pageable);
    }


    @Transactional
    public void batchInsertProducts(List<ProductsDto> productsDto) {
        List<Products> products = new ArrayList<>();
        for (ProductsDto productDto : productsDto) {
            Products existingProduct = productsRepository.findByName(productDto.getName());
            if (existingProduct != null) {
                continue;
            }
            Products product = getProducts(productDto);
            products.add(product);
        }
        if(!products.isEmpty()) {
            productsRepository.saveAll(products);
        }
    }

    public Products findProducts(String textfind) {
        if (textfind.startsWith("BC")) {
            return productsRepository.findByBarcode(textfind);
        } else {
            return productsRepository.findByName(textfind);
        }
    }

    private static Products getProducts(ProductsDto productDto) {
        Products product = new Products();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setBarcode(productDto.getBarcode());
        product.setImage(productDto.getImage());
        product.setWeight(productDto.getWeight());
        product.setHeight(productDto.getHeight());
        product.setWidth(productDto.getWidth());
        product.setLength(productDto.getLength());
        product.setDeleted(false);
        product.setStatus(true);
        return product;
    }

}