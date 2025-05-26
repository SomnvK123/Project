package com.example.project.controller;

import com.example.project.dto.ProductsDto;
import com.example.project.model.Products;
import com.example.project.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/insert")
    @Transactional
    public ResponseEntity<?> insertProducts(@RequestBody List<ProductsDto> productsDto) {
        productService.batchInsertProducts(productsDto);
        return ResponseEntity.ok("Products inserted successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Products>> getAllProducts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id, asc") String[] sort) {
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        Page<Products> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/find/{textfind}")
    public Products findProducts(@PathVariable String textfind) {
        Products products = productService.findProducts(textfind);
        return products;
    }

    @PutMapping("/update/{id}")
    @Transactional
    public ResponseEntity<?> updateProducts(@RequestBody ProductsDto productDto, @PathVariable Integer id) {
        Products product = productService.updateProducts(id, productDto);
        if (product == null) {
            return ResponseEntity.status(404).body("Product not found");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("Product updated successfully: " + product);
    }

    @PutMapping("/softdelete/{id}")
    @Transactional
    public ResponseEntity<?> softDeleteProducts(@PathVariable Integer id) {
        productService.softDeleteProducts(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Product deleted successfully");
    }
}